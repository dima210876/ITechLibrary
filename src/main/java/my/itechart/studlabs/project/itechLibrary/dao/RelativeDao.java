package my.itechart.studlabs.project.itechLibrary.dao;

import my.itechart.studlabs.project.itechLibrary.model.record.RelationRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RelativeDao
{
    Optional<RelationRecord> findById(long id);

    Optional<List<RelationRecord>> findAll();

    Optional<RelationRecord> create(RelationRecord relationRecord);

    boolean delete(long id);

    default Optional<List<RelationRecord>> retrieveRelationRecordsFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<RelationRecord> relationRecords = new ArrayList<>();
        while (resultSet.next()) { relationRecords.add(retrieveRelationRecordFromResultSet(resultSet)); }
        return Optional.of(relationRecords);
    }

    default RelationRecord retrieveRelationRecordFromResultSet(ResultSet resultSet) throws SQLException
    {
        return RelationRecord.builder()
                .id(resultSet.getLong("id"))
                .firstId(resultSet.getLong("book_id"))
                .secondId(resultSet.getLong("author_id"))
                .build();
    }

    default void closeStatement(Statement st)
    {
        if (st != null)
        {
            try { st.close(); }
            catch (SQLException e)
            {
                final Logger LOGGER = LogManager.getLogger(RelativeDao.class);
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
                final Logger LOGGER = LogManager.getLogger(RelativeDao.class);
                LOGGER.error("SQLException while trying to close result set: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
}
