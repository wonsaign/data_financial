package com.deal.datafinancial.config.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wangsai
 */
public class ListDoubleTypeHandler implements TypeHandler<List<Double>> {

    private List<Double> getStringArray(String columnValue) {
        if (Strings.isNullOrEmpty(columnValue)){
            return null;
        }
        return JSON.parseArray(columnValue, Double.class);
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Double> parameter, JdbcType jdbcType) throws SQLException {
        // nop 不允许设值
    }

    @Override
    public List<Double> getResult(ResultSet rs, String columnName) throws SQLException {
        String v = rs.getString(columnName);
        return getStringArray(v);
    }

    @Override
    public List<Double> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        return getStringArray(v);
    }

    @Override
    public List<Double> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String v = cs.getString(columnIndex);
        return getStringArray(v);
    }
}
