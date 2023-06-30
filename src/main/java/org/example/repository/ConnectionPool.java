package org.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionPool {

    private final HikariDataSource ds;
    final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    String url;


    String user;


    String password;


    //      WITH SPRING PROPERTY SOURCE
    public ConnectionPool(
	    @Value("${spring.datasource.url}")
	    String dbUrl,

	    @Value("${spring.datasource.user}")
	    String dbUser,

	    @Value("${spring.datasource.password}")
	    String dbPassword) {
	this.url=dbUrl;
	this.user=dbUser;
	this.password=dbPassword;

	HikariConfig config = new HikariConfig();

	config.setJdbcUrl(dbUrl);
	config.setUsername(dbUser);
	config.setPassword(dbPassword);

	ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
	logger.info(url+"url prishel"+user+"user prishel"+password+"pass prishel");
	return ds.getConnection();
    }
}