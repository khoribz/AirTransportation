WITH fromMockowTable AS
         (SELECT EXTRACT(dow from scheduleddeparture) as day, COUNT(*) as num
          FROM flights
                   INNER JOIN airports a on flights.departureairport = a.airportcode
          WHERE a.city = 'Moscow'
          GROUP BY day, a.city
          ORDER BY day),
     toMoscowTable AS
         (SELECT EXTRACT(dow from scheduleddeparture) as day, COUNT(*) as num
          FROM flights
                   INNER JOIN airports a on flights.arrivalairport = a.airportcode
          WHERE a.city = 'Moscow'
          GROUP BY day, a.city
          ORDER BY day)
SELECT f.day, f.num, t.num
FROM fromMockowTable f
         FULL OUTER JOIN toMoscowTable t ON f.day = t.day
