package com.example.devutils.utils.jdbc;

import com.example.devutils.utils.jdbc.domain.Page;
import com.example.devutils.utils.jdbc.domain.PageImpl;
import com.example.devutils.utils.jdbc.domain.PageRequest;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Created by AMe on 2020-07-10 04:35.
 */
public class JdbcUtils {

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

    public int insert(String sql) {
        return jdbcTemplate.update(sql);
    }

    public int insert(String table, List<String> fields, Object[] values) {
        return insert(table, fields, values, (KeyHolder) null);
    }

    public int insert(String table, List<String> fields, Object[] values, KeyHolder keyHolder) {
        String sql = SqlParseUtils.buildInsertSql(table, fields, false);
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
        String sql = SqlParseUtils.buildBatchInsertSql(table, fields, batchValues.size(), false);
        if (logger.isTraceEnabled()) {
            logger.trace("sql: {}, args: {}", sql, batchValues);
        }
        return jdbcTemplate.batchUpdate(sql, batchValues);
    }

    public int[] batchInsert(String table, Map<String, Object[]> fieldValuesMap) {
        return batchInsert(table, new ArrayList<>(fieldValuesMap.keySet()), new ArrayList<>(fieldValuesMap.values()));
    }

    public int delete(String sql) {
        return jdbcTemplate.update(sql);
    }

    public int delete(String sql, Object[] args, int[] argTypes) {
        return jdbcTemplate.update(sql, args, argTypes);
    }

    public int update(String sql) {
        return jdbcTemplate.update(sql);
    }

    public int update(String sql, Object[] args, int[] argTypes) {
        return jdbcTemplate.update(sql, args, argTypes);
    }

    public <T> T selectValue(String sql, Class<T> clazz) {
        return jdbcTemplate.queryForObject(sql, clazz);
    }

    public <T> T selectValue(String sql, Object[] values, int[] valueTypes, Class<T> clazz) {
        return jdbcTemplate.queryForObject(sql, values, valueTypes, clazz);
    }

    public Map<String, Object> selectRow(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> selectRow(String sql, Object[] values, int[] valueTypes) {
        return jdbcTemplate.queryForMap(sql, values, valueTypes);
    }

    public <T> T selectRow(String sql, Class<T> clazz) {
        return selectRow(sql, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> T selectRow(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    public <T> T selectRow(String sql, Object[] values, int[] valueTypes, Class<T> clazz) {
        return selectRow(sql, values, valueTypes, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> T selectRow(String sql, Object[] values, int[] valueTypes, RowMapper<T> rowMapper) {
        return jdbcTemplate.queryForObject(sql, values, valueTypes, rowMapper);
    }

    public <T> T selectRow(String namedSql, T entity, Class<T> clazz) {
        return selectRow(namedSql, entity, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> T selectRow(String namedSql, T entity, RowMapper<T> rowMapper) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(Objects.requireNonNull(entity));
        return namedParameterJdbcTemplate.queryForObject(namedSql, parameterSource, rowMapper);
    }

    public <T> List<T> selectColumn(String sql, Class<T> clazz) {
        return jdbcTemplate.queryForList(sql, clazz);
    }

    public <T> List<T> selectColumn(String sql, Object[] values, int[] valueTypes, Class<T> clazz) {
        return jdbcTemplate.queryForList(sql, values, valueTypes, clazz);
    }

    public List<Map<String, Object>> selectRows(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> selectRows(String sql, Object[] values, int[] valueTypes) {
        return jdbcTemplate.queryForList(sql, values, valueTypes);
    }

    public <T> List<T> selectRows(String sql, Class<T> clazz) {
        return selectRows(sql, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> List<T> selectRows(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    public <T> List<T> selectRows(String sql, Object[] values, int[] valueTypes, Class<T> clazz) {
        return selectRows(sql, values, valueTypes, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> List<T> selectRows(String sql, Object[] values, int[] valueTypes, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, values, valueTypes, rowMapper);
    }

    public <T> List<T> selectRows(String namedSql, T entity, Class<T> clazz) {
        return selectRows(namedSql, entity, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> List<T> selectRows(String namedSql, T entity, RowMapper<T> rowMapper) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(Objects.requireNonNull(entity));
        return namedParameterJdbcTemplate.query(namedSql, parameterSource, rowMapper);
    }

    public <T> Page<T> selectPage(String sqlWithNoLimit, Class<T> clazz, int page, int size) {
        return selectPage(sqlWithNoLimit, sql -> selectValue(sql, Long.class), sql -> selectRows(sql, clazz), page, size);
    }

    public <T> Page<T> selectPage(String sqlWithNoLimit, Object[] values, int[] valueTypes, Class<T> clazz, int page, int size) {
        return selectPage(sqlWithNoLimit, sql -> selectValue(sql, values, valueTypes, Long.class), sql -> selectRows(sql, values, valueTypes, clazz), page, size);
    }

    private <T> Page<T> selectPage(String sqlWithNoLimit, ToLongFunction<String> countSupplier, Function<String, List<T>> contentSupplier, int page, int size) {
        String countSql = SqlParseUtils.buildCountSql(sqlWithNoLimit);
        long count = Objects.requireNonNull(countSupplier).applyAsLong(countSql);
        if (count == 0) {
            return new PageImpl<>(Collections.emptyList());
        } else {
            if ((sqlWithNoLimit = sqlWithNoLimit.trim()).endsWith(";")) {
                sqlWithNoLimit = sqlWithNoLimit.substring(0, sqlWithNoLimit.length() - 1);
            }
            PageRequest pageRequest = PageRequest.of(page, size);
            String limitClause = String.format(" limit %s, %s;", pageRequest.getOffset(), pageRequest.getPageSize());
            String limitSql = sqlWithNoLimit + limitClause;
            List<T> content = Objects.requireNonNull(contentSupplier).apply(limitSql);
            return new PageImpl<>(content, pageRequest, count);
        }
    }

}
