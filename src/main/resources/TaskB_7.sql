BEGIN;
UPDATE FLIGHTS f
SET status = 'Cancelled'
FROM AIRPORTS a
WHERE (f.departureairport = a.airportcode or f.arrivalairport = a.airportcode)
  and a.city = 'Moscow' and f.scheduleddeparture > ? and f.scheduleddeparture < ?;

SELECT extract(dow from scheduleddeparture) as day, sum(amount)
FROM FLIGHTS f
         JOIN airports a1 on f.arrivalairport = a1.airportcode
         JOIN airports a2 on a2.airportcode = f.departureairport
         JOIN ticket_flights t on f.flightid = t.flightid
WHERE (a1.city = 'Moscow' or a2.city = 'Moscow')
  and f.scheduleddeparture > ? and f.scheduleddeparture < ?
GROUP BY day