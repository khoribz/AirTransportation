package com.khoribz.service.dao;

import lombok.AllArgsConstructor;

import com.khoribz.domain.Aircraft;
import com.khoribz.service.db.SimpleJdbcTemplate;
import com.khoribz.service.parsing.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.sql.ResultSet;
import java.sql.SQLException;


@AllArgsConstructor
public final class AircraftDao {
    private final SimpleJdbcTemplate source;

    private Aircraft createAircrafts(ResultSet resultSet) throws SQLException {
        return new Aircraft(resultSet.getString("aircraftCode"),
                resultSet.getString("model"),
                resultSet.getInt("range"));
    }

    public void saveAircrafts(Collection<Aircraft> aircrafts) throws SQLException {
        source.preparedStatement("INSERT INTO AIRCRAFTS(aircraftCode, model, range) VALUES (?, ?, ?)",
                insertAircraft -> {
                    for (Aircraft aircraft : aircrafts) {
                        insertAircraft.setString(1, aircraft.getAircraftCode());
                        insertAircraft.setString(2, aircraft.getModel());
                        insertAircraft.setInt(3, aircraft.getRange());
                        insertAircraft.execute();
                    }
                });
    }

    public Set<Aircraft> getAircrafts() throws SQLException{
        return source.statement(stmt -> {
            Set<Aircraft> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM AIRCRAFTS");
            while (resultSet.next()) {
                result.add(createAircrafts(resultSet));
            }
            return result;
        });
    }

    public void downloadCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/aircrafts.csv");
        ArrayList<Aircraft> aircrafts = new ArrayList<>();
        try (BufferedReader reader = new
                BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rows = line.split(",\\S");
                String aircraftCode = rows[0];
                String aircraftModel = new JsonParser().parse(rows[1]);
                Integer aircraftRange = Integer.parseInt(rows[2]);
                aircrafts.add(new Aircraft(aircraftCode, aircraftModel, aircraftRange));
            }
            saveAircrafts(aircrafts);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

}