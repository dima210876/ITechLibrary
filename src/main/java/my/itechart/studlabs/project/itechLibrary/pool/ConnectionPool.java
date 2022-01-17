package my.itechart.studlabs.project.itechLibrary.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool
{
    static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private static final ConnectionPool INSTANCE = new ConnectionPool();
    private ArrayBlockingQueue<ConnectionProxy> connections;
    private final List<ConnectionProxy> usedConnections = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    private String url;
    private String username;
    private String password;

    private int MIN_CONNECTIONS_AMOUNT;
    private int MAX_CONNECTIONS_AMOUNT;

    private ConnectionPool() { }

    public static ConnectionPool getInstance() { return INSTANCE; }

    public boolean isEmpty() { return connections.isEmpty(); }

    public Connection retrieveConnection()
    {
        lock.lock();
        ConnectionProxy connectionProxy = null;
        try
        {
            if (connections.size() + usedConnections.size() < MAX_CONNECTIONS_AMOUNT
                    && connections.size() * 3 < usedConnections.size())
            {
                new Thread(this::createNewConnection).start();
            }
            while (isEmpty()) { notEmpty.await(); }
            connectionProxy = connections.take();
            usedConnections.add(connectionProxy);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("InterruptedException during retrieving connection: " + e.getLocalizedMessage());
        }
        finally
        {
            lock.unlock();
        }
        return connectionProxy;
    }

    public void returnConnection(Connection connection)
    {
        if (usedConnections.stream().anyMatch(conn -> conn.equals(connection)))
        {
            lock.lock();
            try
            {
                if (connections.size() + usedConnections.size() > MIN_CONNECTIONS_AMOUNT
                        && connections.size() > usedConnections.size() * 3)
                {
                    ((ConnectionProxy) connection).closeConnection();
                }
                else
                {
                    connections.put((ConnectionProxy) connection);
                    usedConnections.remove(connection);
                    notEmpty.signal();
                }
            }
            catch (InterruptedException e)
            {
                LOGGER.error("InterruptedException during returning connection" + e.getLocalizedMessage());
            }
            finally { lock.unlock(); }
        }
    }

    public void init()
    {
        initParams();
        registerDrivers();
        for (int i = 0; i < MIN_CONNECTIONS_AMOUNT; i++) { createNewConnection(); }
    }

    private void initParams()
    {
        final String fileName = "database.properties";
        try
        {
            InputStream fileInputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            url = properties.getProperty("db.url") + '?' + properties.getProperty("db.timezoneSettings");
            username = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
            MIN_CONNECTIONS_AMOUNT = Integer.parseInt(properties.getProperty("db.minPoolSize"));
            MAX_CONNECTIONS_AMOUNT = Integer.parseInt(properties.getProperty("db.maxPoolSize"));
            connections = new ArrayBlockingQueue<>(MIN_CONNECTIONS_AMOUNT);
        }
        catch (IOException e)
        {
            LOGGER.error("IOException at initialization stage: " + e.getLocalizedMessage());
        }
    }

    private void createNewConnection()
    {
        lock.lock();
        try
        {
            final Connection realConnection = DriverManager.getConnection(url, username, password);
            final ConnectionProxy proxyConnection = new ConnectionProxy(realConnection);
            connections.put(proxyConnection);
            notEmpty.signal();
        }
        catch (InterruptedException | SQLException e)
        {
            LOGGER.error("Exception during connection creating: " + e.getLocalizedMessage());
        }
        finally
        {
            lock.unlock();
        }
    }

    public void destroy()
    {
        connections.forEach(ConnectionProxy::closeConnection);
        deregisterDrivers();
    }

    private static void registerDrivers()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error("Exception at driver registration stage: " + e.getLocalizedMessage());
        }
    }

    private static void deregisterDrivers()
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements())
        {
            try
            {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
            catch (SQLException e)
            {
                LOGGER.error("SQLException at drivers deregister stage: " + e.getSQLState());
            }
        }
    }
}
