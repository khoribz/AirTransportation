package com.khoribz.service.db;

import javax.sql.DataSource;
import javax.sql.PooledConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPoolingDataSource;


public class SimpleJdbcTemplate {
    public final PGPoolingDataSource connectionPool;

    public SimpleJdbcTemplate(PGPoolingDataSource connectionPool) {
        this.connectionPool = connectionPool;
//        String url = "jdbc:postgresql://localhost:5432/postgres";
//        String user = "khoribz";
//        String password = "postgres";
//        this.connectionPool.setUrl(url);
//        this.connectionPool.setUser(user);
//        this.connectionPool.setPassword(password);
    }

    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T object) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T object) throws SQLException, IOException;
    }
    public void connection(SQLConsumer<? super Connection> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        try (Connection conn = connectionPool.getConnection()) {
            consumer.accept(conn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <R> R connection(SQLFunction<? super Connection, ? extends R> function) throws SQLException {
        Objects.requireNonNull(function);
        try (Connection conn = connectionPool.getConnection()) {
            return function.apply(conn);
        }
    }

    public <R> R statement(SQLFunction<? super Statement, ? extends R> function) throws SQLException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                return function.apply(stmt);
            }
        });
    }

    public void statement(SQLConsumer<? super Statement> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                consumer.accept(stmt);
            }
        });
    }

    public <R> R preparedStatement(String sql, SQLFunction<? super PreparedStatement, ? extends R> function) throws SQLException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return function.apply(stmt);
            }
        });
    }

    public void preparedStatement(String sql, SQLConsumer<? super PreparedStatement> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                consumer.accept(stmt);
            }
        });
    }
}