SELECT city, airportCode
FROM AIRPORTS
WHERE city IN
    (SELECT city
     FROM AIRPORTS
     GROUP BY city
     HAVING COUNT(airportCode) > 1)