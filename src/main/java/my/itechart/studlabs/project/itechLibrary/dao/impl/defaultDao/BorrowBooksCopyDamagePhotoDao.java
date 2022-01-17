package my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao;

import my.itechart.studlabs.project.itechLibrary.dao.DefaultDao;
import my.itechart.studlabs.project.itechLibrary.model.entity.BorrowBooksCopyDamagePhoto;
import my.itechart.studlabs.project.itechLibrary.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowBooksCopyDamagePhotoDao implements DefaultDao<BorrowBooksCopyDamagePhoto>
{
    private static final Logger LOGGER = LogManager.getLogger(BorrowBooksCopyDamagePhotoDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();

    private static final String FIND_BORROW_BOOKS_COPY_DAMAGE_PHOTO_BY_ID_SQL =
            "select id, borrow_books_copy_id, damage_photo_path from borrow_books_copy_damage_photos where id = ?";
    private static final String FIND_ALL_BORROW_BOOKS_COPY_DAMAGE_PHOTOS_SQL =
            "select id, borrow_books_copy_id, damage_photo_path from borrow_books_copy_damage_photos";

    private static final String COUNT_COLUMN_NAME = "count";
    private static final String GET_COUNT_OF_BORROW_BOOKS_COPY_DAMAGE_PHOTOS_SQL = "select count(id) as count from borrow_books_copy_damage_photos";

    private static final String CREATE_BORROW_BOOKS_COPY_DAMAGE_PHOTO_SQL = "insert into borrow_books_copy_damage_photos(borrow_books_copy_id, damage_photo_path) value (?,?)";
    private static final String UPDATE_BORROW_BOOKS_COPY_DAMAGE_PHOTO_SQL = "update borrow_books_copy_damage_photos set damage_photo_path = ? where id = ?";
    private static final String DELETE_BORROW_BOOKS_COPY_DAMAGE_PHOTO_SQL = "delete from borrow_books_copy_damage_photos where id = ?";

    private static final String FIND_BORROW_BOOKS_COPY_DAMAGE_PHOTOS_BY_BORROW_BOOKS_COPY_ID_SQL =
            "select id, borrow_books_copy_id, damage_photo_path from borrow_books_copy_damage_photos where borrow_books_copy_id = ?";

    public BorrowBooksCopyDamagePhotoDao() { }

    @Override
    public Optional<BorrowBooksCopyDamagePhoto> findById(long id)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROW_BOOKS_COPY_DAMAGE_PHOTO_BY_ID_SQL);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) { return Optional.of(retrieveBorrowBooksCopyDamagePhotoFromResultSet(resultSet)); }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find damage photo by id: " + e.getLocalizedMessage());
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
    public Optional<List<BorrowBooksCopyDamagePhoto>> findAll()
    {
        Statement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_BORROW_BOOKS_COPY_DAMAGE_PHOTOS_SQL);
            return retrieveBorrowBooksCopyDamagePhotosFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find all damage photos: " + e.getLocalizedMessage());
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
            resultSet = statement.executeQuery(GET_COUNT_OF_BORROW_BOOKS_COPY_DAMAGE_PHOTOS_SQL);
            if (resultSet.next()) { return (resultSet.getInt(COUNT_COLUMN_NAME) + 19) / 20; }
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to count the number of pages of damage photos: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            closeResultSet(resultSet);
            POOL.returnConnection(conn);
        }
        return 0;
    }

    public Optional<List<BorrowBooksCopyDamagePhoto>> findByBorrowBooksCopyId(long borrowBooksCopyId)
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(FIND_BORROW_BOOKS_COPY_DAMAGE_PHOTOS_BY_BORROW_BOOKS_COPY_ID_SQL);
            statement.setLong(1, borrowBooksCopyId);
            resultSet = statement.executeQuery();
            return retrieveBorrowBooksCopyDamagePhotosFromResultSet(resultSet);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to find damage photos by borrow_books_copy_id: " + e.getLocalizedMessage());
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
    public Optional<BorrowBooksCopyDamagePhoto> create(BorrowBooksCopyDamagePhoto borrowBooksCopyDamagePhoto)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(CREATE_BORROW_BOOKS_COPY_DAMAGE_PHOTO_SQL);
            statement.setLong(1, borrowBooksCopyDamagePhoto.getBorrowBooksCopyId());
            statement.setString(2, borrowBooksCopyDamagePhoto.getDamagePhotoPath());
            statement.execute();
            long newId = statement.getGeneratedKeys().getLong("id");
            return findById(newId);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to create new damage photo: " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BorrowBooksCopyDamagePhoto> update(BorrowBooksCopyDamagePhoto borrowBooksCopyDamagePhoto)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(UPDATE_BORROW_BOOKS_COPY_DAMAGE_PHOTO_SQL);
            statement.setString(1, borrowBooksCopyDamagePhoto.getDamagePhotoPath());
            statement.setLong(2, borrowBooksCopyDamagePhoto.getId());
            statement.executeUpdate();
            return Optional.of(borrowBooksCopyDamagePhoto);
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to update damage photo with id = " +
                    borrowBooksCopyDamagePhoto.getId() + ": " + e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(long id)
    {
        PreparedStatement statement = null;
        Connection conn = null;
        try
        {
            conn = POOL.retrieveConnection();
            statement = conn.prepareStatement(DELETE_BORROW_BOOKS_COPY_DAMAGE_PHOTO_SQL);
            statement.setLong(1, id);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            LOGGER.error("SQLException while trying to delete damage photo with id = " + id +
                    ": "+ e.getLocalizedMessage());
        }
        finally
        {
            closeStatement(statement);
            POOL.returnConnection(conn);
        }
        return false;
    }

    private Optional<List<BorrowBooksCopyDamagePhoto>> retrieveBorrowBooksCopyDamagePhotosFromResultSet(ResultSet resultSet) throws SQLException
    {
        List<BorrowBooksCopyDamagePhoto> booksCopyDamagePhotos = new ArrayList<>();
        while (resultSet.next()) { booksCopyDamagePhotos.add(retrieveBorrowBooksCopyDamagePhotoFromResultSet(resultSet)); }
        return Optional.of(booksCopyDamagePhotos);
    }

    private BorrowBooksCopyDamagePhoto retrieveBorrowBooksCopyDamagePhotoFromResultSet(ResultSet resultSet) throws SQLException
    {
        return BorrowBooksCopyDamagePhoto.builder()
                .id(resultSet.getLong("id"))
                .borrowBooksCopyId(resultSet.getLong("borrow_books_copy_id"))
                .damagePhotoPath(resultSet.getString("damage_photo_path"))
                .build();
    }
}
