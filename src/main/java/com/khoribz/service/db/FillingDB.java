package com.khoribz.service.db;

import lombok.AllArgsConstructor;
import com.khoribz.service.dao.*;
import java.io.IOException;

@AllArgsConstructor
public class FillingDB {
    private final SimpleJdbcTemplate source;
    public final void downloadCSV() throws IOException {
        AircraftDao aircraftDao = new AircraftDao(source);
        AirportDao airportDao = new AirportDao(source);
        BookingDao bookingDao = new BookingDao(source);
        BoardingPassDao boardingPassDao = new BoardingPassDao(source);
        FlightDao flightDao = new FlightDao(source);
        SeatDao seatDao = new SeatDao(source);
        TicketDao ticketDao = new TicketDao(source);
        TicketFlightDao ticketFlightDao = new TicketFlightDao(source);

        aircraftDao.downloadCSV();
        airportDao.downloadCSV();
        bookingDao.downloadCSV();
        boardingPassDao.downloadCSV();
        flightDao.downloadCSV();
        seatDao.downloadCSV();
        ticketDao.downloadCSV();
        ticketFlightDao.downloadCSV();
    }
}
