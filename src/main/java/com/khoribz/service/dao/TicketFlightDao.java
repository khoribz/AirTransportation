package com.khoribz.service.dao;

import lombok.AllArgsConstructor;

import com.khoribz.domain.TicketFlight;
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
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;


@AllArgsConstructor
public final class TicketFlightDao {
    private final SimpleJdbcTemplate source;

    private TicketFlight createTicketFlight(ResultSet resultSet) throws SQLException {
        return new TicketFlight(resultSet.getString("ticketNo"),
                resultSet.getInt("flightId"),
                resultSet.getString("fareConditions"),
                resultSet.getBigDecimal("amount")
        );
    }

    public void saveTicketFlights(Collection<TicketFlight> ticketFlights) throws IOException {
        try {
            source.preparedStatement("INSERT INTO TICKET_FLIGHTS VALUES (?, ?, ?, ?)",
                    insertTicketFlight -> {
                        for (TicketFlight ticketFlight : ticketFlights) {
                            insertTicketFlight.setString(1, ticketFlight.getTicketNo());
                            insertTicketFlight.setInt(2, ticketFlight.getFlightId());
                            insertTicketFlight.setString(3, ticketFlight.getFareConditions());
                            insertTicketFlight.setBigDecimal(4, ticketFlight.getAmount());
                            insertTicketFlight.execute();
                        }
                    });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public Set<TicketFlight> getTicketFlights() throws SQLException{
        return source.statement(stmt -> {
            Set<TicketFlight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM TICKET_FLIGHTS");
            while (resultSet.next()) {
                result.add(createTicketFlight(resultSet));
            }
            return result;
        });
    }

    public void downloadCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/ticket_flights.csv");
        ArrayList<TicketFlight> ticketsFlights = new ArrayList<>();
        try (BufferedReader reader = new
                BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            int cnt = 0;
            int permissibleDataLines = 7000;
            while ((line = reader.readLine()) != null && cnt++ < permissibleDataLines) {
                String[] rows = line.split(",");
                String ticketNo = rows[0];
                Integer flightId = Integer.parseInt(rows[1]);
                String fareConditions = rows[2];
                BigDecimal amount = new BigDecimal(rows[3]);

                ticketsFlights.add(new TicketFlight(ticketNo, flightId, fareConditions, amount));
            }
            saveTicketFlights(ticketsFlights);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}