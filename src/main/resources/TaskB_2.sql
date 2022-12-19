SELECT city, COUNT(*) as num
FROM AIRPORTS
INNER JOIN FLIGHTS ON departureAirport = airportCode
WHERE status='Cancelled'
GROUP BY city
ORDER BY num DESC