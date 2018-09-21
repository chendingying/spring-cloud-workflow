package com.spring.cloud.common.client.jdbc;

import com.spring.cloud.common.model.ObjectMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author cdy
 * @create 2018/9/4
 */
public class ObjectMapRowMapper implements RowMapper<ObjectMap> {

    @Override
    public ObjectMap mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        ObjectMap mapOfColValues = new ObjectMap();
        for (int i = 1; i <= columnCount; i++) {
            String key = JdbcUtils.lookupColumnName(rsmd, i);
            Object obj = JdbcUtils.getResultSetValue(rs, i, String.class);
            mapOfColValues.put(key, obj);
        }
        return mapOfColValues;
    }
}
