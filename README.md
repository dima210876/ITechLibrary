# ITechLibrary

# Startup Settings (using IntelliJ IDEA Ultimate) #
* Add run/debug configuration with Tomcat Server Local
* Set http port 8080, jmx port 1099
* Set browser URL http://localhost:8080/iTechLibrary/
* Set deployment application context /iTechLibrary
* Add war exploded artifact to deploy at the server startup
* Change your MySQL database connection properties at database.properties file
* Prepare database tables using database.sql file
* To test application you can use test.sql file (to generate data in tables)
* If you need to restart with the same generated data, just rerun sql-scripts
