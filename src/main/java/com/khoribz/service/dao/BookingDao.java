package com.khoribz.service.dao;

import lombok.AllArgsConstructor;

import com.khoribz.domain.Booking;
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
import java.sql.Timestamp;

@AllArgsConstructor
public final class BookingDao {
    private final SimpleJdbcTemplate source;

    private Booking createBooking(ResultSet resultSet) throws SQLException {
        return new Booking(resultSet.getString("bookRef"),
                resultSet.getTimestamp("bookDate"),
                resultSet.getBigDecimal("totalAmount")
        );
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException, IOException {
        source.preparedStatement("INSERT INTO BOOKINGS VALUES (?, ?, ?)",
                insertBooking -> {
                    for (Booking booking : bookings) {
                        insertBooking.setString(1, booking.getBookRef());
                        insertBooking.setTimestamp(2, booking.getBookDate());
                        insertBooking.setBigDecimal(3, booking.getTotalAmount());
                        insertBooking.execute();
                    }
                });
    }

    public Set<Booking> getBookings() throws SQLException{
        return source.statement(stmt -> {
            Set<Booking> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM BOOKINGS");
            while (resultSet.next()) {
                result.add(createBooking(resultSet));
            }
            return result;
        });
    }

    public void downloadCSV() throws IOException {
        URL url = new URL("https://storage.yandexcloud.net/airtrans-small/bookings.csv");
        ArrayList<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new
                BufferedReader(new InputStreamReader(url.openStream(),
                StandardCharsets.UTF_8))) {
            String line;
            int cnt = 0;
            int permissibleDataLines = 7000;
            while ((line = reader.readLine()) != null && cnt++ < permissibleDataLines) {
                String[] rows = line.split(",");
                String bookRef = rows[0];
                Timestamp bookDate = Timestamp.valueOf(rows[1].substring(0, rows[1].length() - 3));
                BigDecimal totalAmount = new BigDecimal(rows[2]);

                bookings.add(new Booking(bookRef, bookDate, totalAmount));
            }
            saveBookings(bookings);
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }

}