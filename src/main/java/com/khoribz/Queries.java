package com.khoribz;

import com.khoribz.domain.Aircraft;
import com.khoribz.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Timestamp;

@AllArgsConstructor
public class Queries {
    private final SimpleJdbcTemplate source;

    public void one() throws SQLException, IOException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_1.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        }

        source.preparedStatement(query, airports -> {
            var cityWithAirport = new HashMap<String, ArrayList<String>>();
            try (var airport = airports.executeQuery()) {
                while (airport.next()) {
                    String city = airport.getString(1);
                    String airportCode = airport.getString(2);
                    if (!cityWithAirport.containsKey(city)) {
                        cityWithAirport.put(city, new ArrayList<>());
                    }
                    cityWithAirport.get(city).add(airportCode);
                }
            }
            try (FileWriter out = new FileWriter("QueriesResult/TaskB_1")) {
                for (String key : cityWithAirport.keySet()) {
                    StringBuilder airportsList = new StringBuilder();
                    for (var value : cityWithAirport.get(key)) {
                        airportsList.append(value);
                        airportsList.append(", ");
                    }
                    airportsList.delete(airportsList.length() - 2, airportsList.length());
                    try {
                        out.write(key + ": " + airportsList + '\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void two() throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_2.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        source.preparedStatement(query, cities -> {
            var cityWithCancelledFlight = new TreeMap<String, Integer>();
            try (var cityNum = cities.executeQuery()) {
                while (cityNum.next()) {
                    String city = cityNum.getString(1);
                    Integer numCancelled = cityNum.getInt(2);
                    cityWithCancelledFlight.put(city, numCancelled);
                }
            }
            try (FileWriter out = new FileWriter("QueriesResult/TaskB_2")) {
                ArrayList<String> keys = new ArrayList<>(cityWithCancelledFlight.keySet());
                for (String key : keys) {
                    try {
                        out.write(key + ": " + cityWithCancelledFlight.get(key) + '\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void three() throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_3.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        source.preparedStatement(query, statements -> {
            var lines = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < 3; ++i) {
                lines.add(new ArrayList<String>());
            }
            int queryNum = 0;
            try (var line = statements.executeQuery()) {
                while (line.next()) {
                    String cityFrom = line.getString(1);
                    String cityTo = line.getString(2);
                    String duration = line.getString(3);

                    lines.get(0).add(cityFrom);
                    lines.get(1).add(cityTo);
                    lines.get(2).add(duration);
                    ++queryNum;
                }
            }
            try (FileWriter out = new FileWriter("QueriesResult/TaskB_3")) {
                for (int lineNum = 0; lineNum < queryNum; ++lineNum) {
                    try {
                        out.write(lines.get(0).get(lineNum) + "--" +
                                lines.get(1).get(lineNum) + "--" + lines.get(2).get(lineNum) + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void four() throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_4.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        source.preparedStatement(query, months -> {
            var cityWithCancelledFlight = new TreeMap<String, Integer>();
            try (var month = months.executeQuery()) {
                while (month.next()) {
                    String monthNum = month.getString(1);
                    Integer numCancelled = month.getInt(2);
                    cityWithCancelledFlight.put(monthNum, numCancelled);
                }
            }
            try (FileWriter out = new FileWriter("QueriesResult/TaskB_4")) {
                ArrayList<String> keys = new ArrayList<>(cityWithCancelledFlight.keySet());
                for (String key : keys) {
                    try {
                        out.write("Month №" + key + ": " + cityWithCancelledFlight.get(key) + '\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void five() throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_5.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        source.preparedStatement(query, days -> {
            var daysAndDlights = new ArrayList<ArrayList<Integer>>();
            try (var day = days.executeQuery()) {
                int lineNum = 0;
                while (day.next()) {
                    daysAndDlights.add(new ArrayList<>());
                    Integer dayNum = day.getInt(1);
                    Integer fromNum = day.getInt(2);
                    Integer toNum = day.getInt(3);
                    daysAndDlights.get(lineNum).add(dayNum);
                    daysAndDlights.get(lineNum).add(fromNum);
                    daysAndDlights.get(lineNum).add(toNum);
                    ++lineNum;
                }
            }
            try (FileWriter out = new FileWriter("QueriesResult/TaskB_5")) {
                for (var line : daysAndDlights) {
                    try {
                        out.write("Day №" + (line.get(0) + 1) + " --- From Moscow: " +
                                line.get(1) + " --- To Moscow: " + line.get(2) + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void six(String model) throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_6.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] queryParts = query.split(";");
        for (String part : queryParts) {
            if ("BEGIN".equals(part)) {
                source.statement(stmt -> {
                    stmt.execute(part);
                });
            } else {
                source.preparedStatement(part,
                        cancellFlight -> {
                            cancellFlight.setString(1, model);
                            cancellFlight.execute();
                        });
            }
        }
    }

    public void seven(Timestamp fromTime, Timestamp toTime) throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_6.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] queryParts = query.split(";");
        for (String part : queryParts) {
            if ("BEGIN".equals(part)) {
                source.statement(stmt -> {
                    stmt.execute(part);
                });
            } else {
                source.preparedStatement(part,
                        cancellFlight -> {
                            cancellFlight.setTimestamp(1, fromTime);
                            cancellFlight.setTimestamp(1, toTime);
                            cancellFlight.execute();
                        });
            }
        }
    }

    public void eight(String ticketNo, String bookRef, String passengerId, String passengerName,
                      String contactData, String flightNo, String seatNo) throws SQLException {
        String query = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(App.class.getResourceAsStream("/TaskB_8.sql")),
                        StandardCharsets.UTF_8))) {
            query = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        source.preparedStatement(query,
                script -> {
                    script.setString(1, flightNo);
                    script.setString(2, seatNo);
                    script.setString(3, ticketNo);
                    script.setString(4, bookRef);
                    script.setString(5, passengerId);
                    script.setString(6, passengerName);
                    script.setString(7, contactData);
                    script.execute();
                });
    }
}
