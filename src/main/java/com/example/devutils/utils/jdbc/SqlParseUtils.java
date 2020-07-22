package com.example.devutils.utils.jdbc;

import com.example.devutils.utils.collection.ListUtils;
import com.example.devutils.utils.text.RegexUtils;
import com.example.devutils.utils.text.StringUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.util.CollectionUtils;

/**
 * Created by AMe on 2020-07-22 17:26.
 */
public class SqlParseUtils {

    public static final String OPT_INSERT = "insert";
    public static final String OPT_DELETE = "delete";
    public static final String OPT_UPDATE = "update";
    public static final String OPT_SELECT = "select";

    private static final String insertTemplate = "insert into %s %s values %s";
    private static final String updateTemplate = "update %s set %s where %s";
    private static final String deleteTemplate = "delete from %s where %s";

    public static String getTableName(String sql, String opt, String regex) {
        if (StringUtils.isNotBlank(sql) && (sql = sql.trim()).startsWith(opt)) {
            String[] result = RegexUtils.findOne(sql, regex, true);
            if (result.length >= 2) {
                return result[1];
            }
        }
        return null;
    }

    public static String getInsertTable(String insertSql) {
        String regex = "^insert +[ignore| ]*into +`?(.*?)`?[\\(| ]+";
        return getTableName(insertSql, OPT_INSERT, regex);
    }

    public static String getDeleteTable(String deleteSql) {
        String regex = "^delete +from +`?(.*?)`? +where";
        return getTableName(deleteSql, OPT_DELETE, regex);
    }

    public static String getUpdateTable(String updateSql) {
        String regex = "^update +`?(.*?)`? +set";
        return getTableName(updateSql, OPT_UPDATE, regex);
    }

    public static String getSelectTable(String selectSql) {
        String regex = " +from +`?(.*?)`? +";
        return getTableName(selectSql, OPT_SELECT, regex);
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

    public static String buildCountSql(String selectSql) {
        if (StringUtils.isNotBlank(selectSql) && (selectSql = selectSql.trim()).startsWith(OPT_SELECT)) {
            int fromIndex = selectSql.indexOf("from");
            if (fromIndex != -1) {
                return "select count(*) as count " + selectSql.substring(fromIndex);
            }
        }
        return null;
    }

}
