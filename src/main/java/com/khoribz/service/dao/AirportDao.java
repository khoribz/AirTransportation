package com.khoribz.service.dao;

import com.khoribz.service.parsing.JsonParser;
import lombok.AllArgsConstructor;

import com.khoribz.domain.Airport;
import com.khoribz.service.db.SimpleJdbcTemplate;

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
public final class AirportDao {
    private final SimpleJdbcTemplate source;

    private Airport createAirports(ResultSet resultSet) throws SQLException {
        return new Airport(resultSet.getString("airportCode"),
                resultSet.getString("airportName"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone")
        );
    }

    public void saveAirports(Collection<Airport> airports) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO AIRPORTS VALUES (?, ?, ?, ?, ?)",
                insertAirport -> {
                    for (Airport airport : airports) {
                        insertAirport.setString(1, airport.getAirportCode());
                        insertAirport.setString(2, airport.getAirportName());
                        insertAirport.setString(3, airport.getCity());
                        insertAirport.setString(4, airport.getCoordinates());
                        insertAirport.setString(5, airport.getTimezone());
                        insertAirport.execute();
                    }
                });
    }

    public Set<Airport> getAirports() throws SQLException {
        return source.statement(stmt -> {
            Set<Airport> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM AIRPORTS");
            while (resultSet.next()) {
                result.add(createAirports(resultSet));
            }
            return result;
        });
    }

    public void downloadCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/airports.csv");
        ArrayList<Airport> airports = new ArrayList<>();
        try (BufferedReader reader = new
                BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rows = line.split(",");
                String airportCode = rows[0];
                String airportName = new JsonParser().parse(rows[1].substring(1) + "," + rows[2]);
                String city = new JsonParser().parse(rows[3].substring(1) + "," + rows[4]);
                String coordinates = rows[5].substring(1) + "," + rows[6].substring(0, rows[6].length() - 1);
                String timezone = rows[7];

                airports.add(new Airport(airportCode, airportName, city, coordinates, timezone));
            }
            saveAirports(airports);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }
}