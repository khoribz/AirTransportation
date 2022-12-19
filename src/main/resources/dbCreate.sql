CREATE TABLE IF NOT EXISTS AIRCRAFTS (
    aircraftCode CHAR(3) NOT NULL,
    model VARCHAR NOT NULL,
    range INTEGER NOT NULL,
    PRIMARY KEY (aircraftCode)
);

CREATE TABLE IF NOT EXISTS AIRPORTS (
    airportCode CHAR(3),
    airportName VARCHAR NOT NULL,
    city VARCHAR NOT NULL,
    coordinates VARCHAR NOT NULL,
    timezone VARCHAR NOT NULL,
    PRIMARY KEY (airportCode)
);

CREATE TABLE IF NOT EXISTS BOARDING_PASSES (
    ticketNo CHAR(13) NOT NULL,
    flightId INTEGER NOT NULL,
    boardingNo INTEGER NOT NULL,
    seatNo VARCHAR(4) NOT NULL,
    CONSTRAINT boardingPassesPK PRIMARY KEY(ticketNo, flightId)
);

CREATE TABLE IF NOT EXISTS BOOKINGS (
    bookRef CHAR(6),
    bookDate TIMESTAMP NOT NULL,
    totalAmount DECIMAL NOT NULL,
    PRIMARY KEY (bookRef)
);

CREATE TABLE IF NOT EXISTS FLIGHTS (
    flightId INTEGER,
    flightNo CHAR(6) NOT NULL,
    scheduledDeparture TIMESTAMP NOT NULL,
    scheduledArrival TIMESTAMP NOT NULL,
    departureAirport CHAR(3) NOT NULL REFERENCES AIRPORTS(airportCode),
    arrivalAirport CHAR(3) NOT NULL REFERENCES AIRPORTS(airportCode),
    status VARCHAR(20) NOT NULL,
    aircraftCode CHAR(3) NOT NULL REFERENCES AIRCRAFTS(aircraftCode),
    actualDeparture TIMESTAMP,
    actualArrival TIMESTAMP,
    PRIMARY KEY (flightId)
);

CREATE TABLE IF NOT EXISTS SEATS (
    aircraftCode CHAR(3) NOT NULL REFERENCES AIRCRAFTS(aircraftCode),
    seatNo VARCHAR(4) NOT NULL,
    fareConditions VARCHAR(10) NOT NULL,
    CONSTRAINT seatsPK PRIMARY KEY(aircraftCode, seatNo)
);

CREATE TABLE IF NOT EXISTS TICKETS (
    ticketNo CHAR(13),
    bookRef CHAR(6) NOT NULL REFERENCES BOOKINGS(bookRef),
    passengerId VARCHAR(20) NOT NULL,
    passengerName VARCHAR NOT NULL,
    contactData VARCHAR,
    PRIMARY KEY (ticketNo)
);

CREATE TABLE IF NOT EXISTS TICKET_FLIGHTS (
    ticketNo CHAR(13) NOT NULL,
    flightId INTEGER NOT NULL,
    fareConditions VARCHAR(10) NOT NULL,
    amount DECIMAL NOT NULL,
    CONSTRAINT ticketFlightsPk PRIMARY KEY(ticketNo, flightId)
);