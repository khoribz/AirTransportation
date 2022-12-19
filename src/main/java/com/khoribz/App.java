package com.khoribz;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.khoribz.service.db.CreationDB;
import com.khoribz.service.db.FillingDB;
import com.khoribz.service.db.RemovalDB;
import com.khoribz.service.db.SimpleJdbcTemplate;
import org.postgresql.ds.PGPoolingDataSource;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws SQLException, IOException {
        PGPoolingDataSource connectionPool = new PGPoolingDataSource();
        connectionPool.setUrl("jdbc:postgresql://localhost:5432/postgres");
        connectionPool.setUser("khoribz");
        connectionPool.setPassword("postgres");
        SimpleJdbcTemplate source = new SimpleJdbcTemplate(connectionPool);

        var databaseInitialization = new CreationDB(source);
        databaseInitialization.create();

        var databaseFilling = new FillingDB(source);
        databaseFilling.downloadCSV();
        new Queries(source).one();
        new Queries(source).two();
        new Queries(source).three();
        new Queries(source).four();
        new Queries(source).five();
        new Queries(source).six("Boeing 777-300");
        new Queries(source).seven(Timestamp.valueOf("2017-09-10 09:50:00.000000"),
                Timestamp.valueOf("2017-012-10 11:50:00.000000"));
        new Queries(source).eight("1", "1", "1", "2", "3", "4", "5");

        var databaseRemoval = new RemovalDB(source);
        databaseRemoval.remove();
    }
}
