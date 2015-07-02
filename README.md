BusService was a homework project that I did for my Java Servlets and JSP class. It is a web service that communicates with a backend MySQL database which contains a (fictitious) database of bus data.

The schema of the database table buses_table I was given is:
 
BID int(3) primary key,
bus_id VARCHAR (4),
day VARCHAR (50),
bus_time TIME;

where day holds values only 'Saturday', 'Sunday' or 'Weekday'


The assignment was to create a web service that returns a String containing all buses that will arrive within the next 15 minutes.

Example: 
Change time to 11:50 pm Sunday
returns buses between Sunday 11:50 pm and Weekday 12:10 am
returns "D1 Sunday 23:55:00 <br />D1 Weekday 00:03:00 <br />"