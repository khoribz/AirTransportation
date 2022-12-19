package com.khoribz.service.db;

import com.khoribz.App;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Initializes database
 */
@AllArgsConstructor
public class CreationDB {
    private final SimpleJdbcTemplate source;

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream(name)),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void create() throws SQLException, IOException {
        String[] queries = getSQL("/dbCreate.sql").split(";");
        for (String query : queries) {
            source.statement(stmt -> {
                stmt.execute(query);
            });
        }
    }
}