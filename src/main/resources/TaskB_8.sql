DO $$ DECLARE
BEGIN
    IF (EXISTS(SELECT flightNo FROM FLIGHTS WHERE flightNo = ?) and
        EXISTS(SELECT seatNo FROM SEATS s JOIN FLIGHTS f
                                               ON s.aircraftcode = f.aircraftcode
               WHERE seatNo = ?))
    THEN INSERT INTO TICKETS VALUES(?, ?, ?, ?, ?);
    END IF;
END
$$ LANGUAGE plpgsql;