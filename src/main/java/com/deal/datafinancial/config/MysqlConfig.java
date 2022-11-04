package com.deal.datafinancial.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author wangsai
 */
@Configuration
@MapperScan(basePackages = {"com.deal.datafinancial.dao"},
            sqlSessionFactoryRef = "mysqlSessionFactory",
            sqlSessionTemplateRef = "mysqlSessionTemplate")
public class MysqlConfig {

    /**
     * 第一个数据连接，默认优先级最高
     * @return
     */
    @Bean(name = "mysqlDataSource")
    @Qualifier("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "mysqlSessionFactory")
    public SqlSessionFactory mysqlSessionFactory(@Qualifier("mysqlDataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        return bean.getObject();
    }

    @Bean("mysqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(
            @Qualifier("mysqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }

//    @Bean("mysqlTransactionManager")
//    public DataSourceTransactionManager transactionManager(@Qualifier("mysqlDataSource")DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}