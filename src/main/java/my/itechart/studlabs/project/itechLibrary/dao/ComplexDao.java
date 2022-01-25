package my.itechart.studlabs.project.itechLibrary.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public interface ComplexDao<T>
{
    Optional<T> findById(long id);

    Optional<List<T>> findAll();

    int getCountOfPages();

    Optional<T> create(Connection conn, T entity);

    Optional<T> update(Connection conn, T entity);

    boolean delete(long id);

    default void closeStatement(Statement st)
    {
        if (st != null)
        {
            try { st.close(); }
            catch (SQLException e)
            {
                final Logger LOGGER = LogManager.getLogger(ComplexDao.class);
                LOGGER.error("SQLException while trying to close statement: " + e.getLocalizedMessage());
            }
        }
    }

    default void closeResultSet(ResultSet rs)
    {
        if (rs != null)
        {
            try { rs.close(); }
            catch (SQLException e)
            {
                final Logger LOGGER = LogManager.getLogger(ComplexDao.class);
                LOGGER.error("SQLException while trying to close result set: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
}
