BEGIN;
UPDATE FLIGHTS f
SET status = 'Cancelled'
FROM AIRCRAFTS a
WHERE f.aircraftcode = a.aircraftcode and model = ?;

DELETE
FROM TICKETS
WHERE ticketno IN (SELECT tf.ticketno
                   FROM TICKET_FLIGHTS as tf JOIN FLIGHTS as fl ON
                   tf.flightId = fl.flightId JOIN TICKETS as tick ON tf.ticketNo = tick.ticketNo
                                             JOIN AIRCRAFTS as aircr ON aircr.aircraftCode = fl.aircraftCode
                   WHERE model = ?);
DELETE
FROM TICKET_FLIGHTS
WHERE flightid IN (SELECT fl.flightid
                   FROM TICKET_FLIGHTS as tf JOIN FLIGHTS as fl ON
                  tf.flightId = fl.flightId  JOIN AIRCRAFTS as aircr ON aircr.aircraftCode = fl.aircraftCode
                   WHERE model = ?);