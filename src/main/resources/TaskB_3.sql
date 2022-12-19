SELECT DISTINCT q.city, cityTo.city, duration
FROM
    (SELECT city, arrivalairport, duration, MIN(duration) OVER (PARTITION BY city) as min_dur
    FROM
        (SELECT city, arrivalairport, TO_CHAR(scheduledarrival - scheduleddeparture, 'HH24:MI') as duration
        FROM flights
        INNER JOIN airports a on flights.departureairport = a.airportcode) as t) as q
INNER JOIN airports cityTo ON cityTo.airportcode = q.arrivalairport
WHERE duration = min_dur
ORDER BY duration