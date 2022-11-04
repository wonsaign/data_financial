package com.deal.datafinancial.config.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wangsai
 */
public class ListBigDecimalTypeHandler implements TypeHandler<List<BigDecimal>> {

    private List<BigDecimal> getStringArray(String columnValue) {
        if (Strings.isNullOrEmpty(columnValue)){
            return null;
        }
        return JSON.parseArray(columnValue, BigDecimal.class);
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, List<BigDecimal> parameter, JdbcType jdbcType) throws SQLException {
        // nop 不允许设值
    }

    @Override
    public List<BigDecimal> getResult(ResultSet rs, String columnName) throws SQLException {
        String v = rs.getString(columnName);
        return getStringArray(v);
    }

    @Override
    public List<BigDecimal> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        return getStringArray(v);
    }

    @Override
    public List<BigDecimal> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String v = cs.getString(columnIndex);
        return getStringArray(v);
    }
}
