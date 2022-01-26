package my.itechart.studlabs.project.itechLibrary.dao.impl.complexDao;

import my.itechart.studlabs.project.itechLibrary.dao.ComplexDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.Borrow;
import my.itechart.studlabs.project.itechLibrary.model.factory.BorrowFactory;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowDao implements ComplexDao<Borrow>
{
    private static final Logger LOGGER = LogManager.getLogger(BorrowDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_BORROW_BY_ID_SQL =
            "select id, reader_id, borrow_date, borrow_time_period_days, return_date, borrow_status, discount_percent, cost from borrows where id = ?";
    private static final String FIND_ALL_BORROWS_SQL =
            "select id, reader_id, borrow_date, borrow_time_period_days, return_date, borrow_status, discount_percent, cost from borrows";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_ALL_BORROWS_SQL = "select count(id) as count from borrows";

    private static final String CREATE_BORROW_SQL =
            "insert into borrows(reader_id, borrow_date, borrow_time_period_days, return_date, borrow_status, discount_percent, cost) value (?,?,?,?,?,?,?)";
    private static final String UPDATE_BORROW_SQL =
            "update borrows set borrow_date = ?, borrow_time_period_days = ?, return_date = ?, borrow_status = ?, discount_percent = ?, cost = ? where id = ?";
    private static final String UPDATE_BORROW_COST_AND_STATUS_TO_RETURNED_SQL =
            "update borrows set cost = ?, borrow_status = 'returned' where id = ?";
    private static final String DELETE_BORROW_SQL = "delete from borrows where id = ?";

    private static final String FIND_BORROWS_BY_READER_ID_SQL =
            "select id, reader_id, borrow_date, borrow_time_period_days, return_date, borrow_status, discount_percent, cost from borrows where reader_id = ?";
    private static final String FIND_ACTIVE_BORROWS_BY_READER_ID_SQL =
            "select id, reader_id, borrow_date, borrow_time_period_days, return_date, borrow_status, discount_percent, cost from borrows where reader_id = ? and borrow_status != 'returned'";

    private static final String FIND_EXPIRED_BORROWS_SQL =
            "select id, reader_id, borrow_date, borrow_time_period_days, return_date, borrow_status, discount_percent, cost from borrows where borrow_status = 'expired'";
    private static final String UPDATE_BORROW_STATUS_TO_EXPIRED_SQL =
            "update borrows set borrow_status = 'expired' where borrow_status = 'active' and return_date < CURDATE()";

    public BorrowDao() { }

    @Override
    public Optional<Borrow> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROW_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveBorrowFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find borrow by id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Borrow>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_BORROWS_SQL);
            return retrieveBorrowsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all borrows: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public int getCountOfPages()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(GET_COUNT_OF_ALL_BORROWS_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of all borrows: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<Borrow>> findAllByReaderId(long readerId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROWS_BY_READER_ID_SQL);
            statement.setLong(1, readerId);
            resultSet = statement.executeQuery();
            return retrieveBorrowsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all borrows by reader id: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<List<Borrow>> findNotReturnedByReaderId(long readerId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_ACTIVE_BORROWS_BY_READER_ID_SQL);
            statement.setLong(1, readerId);
            resultSet = statement.executeQuery();
            return retrieveBorrowsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find not returned borrows by reader id: "
                    + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<List<Borrow>> findExpired()
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_EXPIRED_BORROWS_SQL);
            resultSet = statement.executeQuery();
            return retrieveBorrowsFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find expired borrows: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    public boolean updateBorrowStatusToExpired()
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(UPDATE_BORROW_STATUS_TO_EXPIRED_SQL);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update borrow status: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    public boolean updateBorrowCostAndStatusToReturned(Connection conn, long borrowId, double newCost)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(UPDATE_BORROW_COST_AND_STATUS_TO_RETURNED_SQL);
            statement.setDouble(1, newCost);
            statement.setLong(2, borrowId);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update borrow cost and status to 'returned': "
                    + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return false;
    }

    @Override
    public long create(Connection conn, Borrow borrow)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(CREATE_BORROW_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, borrow.getReaderId());
            statement.setDate(2, Date.valueOf(borrow.getBorrowDate()));
            statement.setInt(3, borrow.getBorrowTimePeriodDays());
            statement.setDate(4, Date.valueOf(borrow.getReturnDate()));
            statement.setString(5, borrow.getBorrowStatus());
            statement.setInt(6, borrow.getDiscountPercent());
            statement.setDouble(7, borrow.getCost());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getLong(1) : 0L; //id of created borrow, otherwise 0
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new borrow: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return 0L;
    }

    @Override
    public boolean update(Connection conn, Borrow borrow)
    {
        PreparedStatement statement = null;
        try
        {
            statement = conn.prepareStatement(UPDATE_BORROW_SQL);
            statement.setDate(1, Date.valueOf(borrow.getBorrowDate()));
            statement.setInt(2, borrow.getBorrowTimePeriodDays());
            statement.setDate(3, Date.valueOf(borrow.getReturnDate()));
            statement.setString(4, borrow.getBorrowStatus());
            statement.setInt(5, borrow.getDiscountPercent());
            statement.setDouble(6, borrow.getCost());
            statement.setLong(7, borrow.getId());
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update borrow with id = " + borrow.getId() +
                    ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
        }
        return false;
    }

    @Override
    public boolean delete(long id)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(DELETE_BORROW_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete borrow with id = " + id +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<Borrow>> retrieveBorrowsFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<Borrow> borrows = new ArrayList<>();
        while (resultSet.next()) { borrows.add(retrieveBorrowFromResultSet(resultSet)); }
        return Optional.of(borrows);
    }

    private Borrow retrieveBorrowFromResultSet(ResultSet resultSet) throws SQLException
    {
        return BorrowFactory.getInstance().create(resultSet);
    }
}
