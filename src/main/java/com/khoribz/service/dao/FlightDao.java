package com.khoribz.service.dao;

import lombok.AllArgsConstructor;

import com.khoribz.domain.Flight;
import com.khoribz.service.db.SimpleJdbcTemplate;
import com.khoribz.service.parsing.TimestampParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public final class FlightDao {
    private final SimpleJdbcTemplate source;

    private Flight createFlight(ResultSet resultSet) throws SQLException {
        return new Flight(resultSet.getInt("flightId"),
                resultSet.getString("flightNo"),
                resultSet.getTimestamp("scheduledDeparture"),
                resultSet.getTimestamp("scheduledArrival"),
                resultSet.getString("departureAirport"),
                resultSet.getString("arrivalAirport"),
                resultSet.getString("status"),
                resultSet.getString("aircraftCode"),
                resultSet.getTimestamp("actualDeparture"),
                resultSet.getTimestamp("actualArrival")
        );
    }

    public void saveFlights(Collection<Flight> flights) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO FLIGHTS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                insertFlight -> {
                    for (Flight flight : flights) {
                        int parameterIndex = 1;
                        insertFlight.setInt(parameterIndex++, flight.getFlightId());
                        insertFlight.setString(parameterIndex++, flight.getFlightNo());
                        insertFlight.setTimestamp(parameterIndex++, flight.getScheduledDeparture());
                        insertFlight.setTimestamp(parameterIndex++, flight.getScheduledArrival());
                        insertFlight.setString(parameterIndex++, flight.getDepartureAirport());
                        insertFlight.setString(parameterIndex++, flight.getArrivalAirport());
                        insertFlight.setString(parameterIndex++, flight.getStatus());
                        insertFlight.setString(parameterIndex++, flight.getAircraftCode());
                        insertFlight.setTimestamp(parameterIndex++, flight.getActualDeparture());
                        insertFlight.setTimestamp(parameterIndex, flight.getActualArrival());
                        insertFlight.execute();
                    }
                });
    }

    public Set<Flight> getFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<Flight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM FLIGHTS");
            while (resultSet.next()) {
                result.add(createFlight(resultSet));
            }
            return result;
        });
    }

    public void downloadCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/flights.csv");
        ArrayList<Flight> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            int cnt = 0;
            int permissibleDataLines = 500;
            while ((line = reader.readLine()) != null && cnt++ < permissibleDataLines) {
                String[] rows = line.split(",");
                int rowNumber = 0;
                Integer flightId = Integer.parseInt(rows[rowNumber++]);
                String flightNo = rows[rowNumber++];
                Timestamp scheduledDeparture = Timestamp.valueOf(new TimestampParser().parse(rows[rowNumber]));
                ++rowNumber;
                Timestamp scheduledArrival = Timestamp.valueOf(new TimestampParser().parse(rows[rowNumber]));
                ++rowNumber;
                String departureAirport = rows[rowNumber++];
                String arrivalAirport = rows[rowNumber++];
                String status = rows[rowNumber++];
                String aircraftCode = rows[rowNumber++];
                Timestamp actualDeparture = null;
                Timestamp actualArrival = null;

                if (rowNumber + 1 <= rows.length) {
                    actualDeparture = Timestamp.valueOf(new TimestampParser().parse(rows[rowNumber]));
                    ++rowNumber;
                }

                if (rowNumber + 1 <= rows.length) {
                    actualArrival = Timestamp.valueOf(new TimestampParser().parse(rows[rowNumber]));
                    ++rowNumber;
                }

                flights.add(new Flight(flightId, flightNo, scheduledDeparture, scheduledArrival, departureAirport,
                        arrivalAirport, status, aircraftCode, actualDeparture, actualArrival));
            }
            saveFlights(flights);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

}