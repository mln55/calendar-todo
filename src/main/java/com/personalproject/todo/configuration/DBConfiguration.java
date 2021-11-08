package com.personalproject.todo.configuration;

import com.personalproject.todo.member.Auth;
import com.personalproject.todo.member.Member;
import com.personalproject.todo.todo.Todo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    ApplicationContext applicationContext;

    public DBConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();

        bean.setDataSource(dataSource);
        bean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*Mapper.xml"));
        bean.setTypeAliases(new Class[] {
            Member.class,
            Todo.class
        });
        bean.setTypeHandlers(new TypeHandler[] {
            new AuthTypeHandler()
        });

        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // VARCHAR타입의 auth를 enum Auth로 매핑하기 위한 핸들러 생성 (임시)
    class AuthTypeHandler extends BaseTypeHandler<Auth> {
        @Override
        public Auth getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return Auth.of(rs.getString(columnName));
        }
    
        @Override
        public Auth getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return Auth.of(rs.getString(columnIndex));
        }
    
        @Override
        public Auth getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return Auth.of(cs.getString(columnIndex));
        }
    
        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, Auth parameter, JdbcType jdbcType)
                throws SQLException {
            ps.setString(i, parameter == null ? null : parameter.name());
        }
    
        
    }
}
