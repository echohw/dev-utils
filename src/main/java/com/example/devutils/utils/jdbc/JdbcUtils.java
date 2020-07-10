package com.example.devutils.utils.jdbc;

import com.example.devutils.utils.collection.ListUtils;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

/**
 * Created by AMe on 2020-07-10 04:35.
 */
public class JdbcUtils {

    private static final String insertTemplate = "insert into %s %s values %s";
    private static final String updateTemplate = "update %s set %s where %s";
    private static final String deleteTemplate = "delete from %s where %s";

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    private JdbcTemplate jdbcTemplate;

    public JdbcUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate);
    }

    public JdbcTemplate jdbcTemplate() {
        return this.jdbcTemplate;
    }

    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }

    public int insert(String table, List<String> fields, Object[] values) {
        return insert(table, fields, values, (KeyHolder) null);
    }

    public int insert(String table, List<String> fields, Object[] values, KeyHolder keyHolder) {
        String sql = buildInsertSql(table, fields, false);
        if (logger.isTraceEnabled()) {
            logger.trace("sql: {}, args: {}", sql, values);
        }
        if (keyHolder != null) {
            return jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < fields.size(); i++) {
                    preparedStatement.setObject(i + 1, values[i]);
                }
                return preparedStatement;
            }, keyHolder);
        } else {
            return jdbcTemplate.update(sql, values);
        }
    }

    public int insert(String table, Map<String, Object> fieldValueMap) {
        return insert(table, fieldValueMap, (KeyHolder) null);
    }

    public int insert(String table, Map<String, Object> fieldValueMap, KeyHolder keyHolder) {
        return insert(table, new ArrayList<>(fieldValueMap.keySet()), fieldValueMap.values().toArray(), keyHolder);
    }

    public int[] batchInsert(String table, List<String> fields, List<Object[]> batchValues) {
        String sql = buildBatchInsertSql(table, fields, batchValues.size(), false);
        if (logger.isTraceEnabled()) {
            logger.trace("sql: {}, args: {}", sql, batchValues);
        }
        return jdbcTemplate.batchUpdate(sql, batchValues);
    }

    public int[] batchInsert(String table, Map<String, Object[]> fieldValuesMap) {
        return batchInsert(table, new ArrayList<>(fieldValuesMap.keySet()), new ArrayList<>(fieldValuesMap.values()));
    }

    public Map<String, Object> selectOne(String sql, Object[] values, int[] valueTypes) {
        return jdbcTemplate.queryForMap(sql, values, valueTypes);
    }

    public <T> T selectOne(String sql, Object[] values, int[] valueTypes, Class<T> clazz) {
        return jdbcTemplate.queryForObject(sql, values, valueTypes, new BeanPropertyRowMapper<>(clazz));
    }

    public List<Map<String, Object>> selectAll(String sql, Object[] values, int[] valueTypes) {
        return jdbcTemplate.queryForList(sql, values, valueTypes);
    }

    public <T> List<T> selectAll(String sql, Object[] values, int[] valueTypes, Class<T> clazz) {
        return jdbcTemplate.query(sql, values, valueTypes, new BeanPropertyRowMapper<>(clazz));
    }

    public static String formatFields(List<String> fieldList, boolean escape) {
        if (CollectionUtils.isEmpty(fieldList)) {
            return "";
        }
        Stream<String> stream = fieldList.stream();
        if (escape) {
            stream = stream.map(name -> "`" + name + "`");
        }
        return stream.collect(Collectors.joining(",", "(", ")"));
    }

    public static String buildInsertSql(String table, List<String> fields, boolean named) {
        return buildBatchInsertSql(table, fields, 1, named);
    }

    public static String buildBatchInsertSql(String table, List<String> fields, int batchSize, boolean named) {
        String fieldsStr = formatFields(fields, true);
        String valuesStr = formatFields(named ? fields.stream().map(":"::concat).collect(
            Collectors.toList()) : ListUtils.of(fields.size(), "?"), false);
        String batchValuesStr = IntStream.range(0, batchSize).boxed().map(t_ -> valuesStr)
            .collect(Collectors.joining(","));
        return String.format(insertTemplate, table, fieldsStr, batchValuesStr);
    }

}
