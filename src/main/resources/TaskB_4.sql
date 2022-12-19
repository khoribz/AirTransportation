SELECT to_char(scheduleddeparture, 'MM') as month, COUNT(*)
FROM flights
WHERE status = 'Cancelled'
GROUP BY month
ORDER BY month ASC