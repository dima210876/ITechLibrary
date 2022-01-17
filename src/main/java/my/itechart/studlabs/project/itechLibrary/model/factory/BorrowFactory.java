package my.itechart.studlabs.project.itechLibrary.model.factory;

import my.itechart.studlabs.project.itechLibrary.model.dto.BorrowDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.Borrow;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowFactory
{
    private static final BorrowFactory INSTANCE = new BorrowFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String READER_ID_COLUMN_NAME = "reader_id";
    private static final String BORROW_DATE_COLUMN_NAME = "borrow_date";
    private static final String BORROW_TIME_PERIOD_DAYS_COLUMN_NAME = "borrow_time_period_days";
    private static final String RETURN_DATE_COLUMN_NAME = "return_date";
    private static final String BORROW_STATUS_COLUMN_NAME = "borrow_status";
    private static final String DISCOUNT_PERCENT_COLUMN_NAME = "discount_percent";
    private static final String COST_COLUMN_NAME = "cost";

    private BorrowFactory() { }

    public static BorrowFactory getInstance() { return INSTANCE; }

    public Borrow create(ResultSet resultSet) throws SQLException
    {
        return Borrow.builder()
                .id(resultSet.getLong(ID_COLUMN_NAME))
                .readerId(resultSet.getLong(READER_ID_COLUMN_NAME))
                .borrowDate(resultSet.getDate(BORROW_DATE_COLUMN_NAME).toLocalDate())
                .borrowTimePeriodDays(resultSet.getInt(BORROW_TIME_PERIOD_DAYS_COLUMN_NAME))
                .returnDate(resultSet.getDate(RETURN_DATE_COLUMN_NAME).toLocalDate())
                .borrowStatus(resultSet.getString(BORROW_STATUS_COLUMN_NAME))
                .discountPercent(resultSet.getInt(DISCOUNT_PERCENT_COLUMN_NAME))
                .cost(resultSet.getDouble(COST_COLUMN_NAME))
                .build();
    }

    public Borrow create(BorrowDto borrowDto)
    {
        return Borrow.builder()
                .id(borrowDto.getId())
                .readerId(borrowDto.getReader().getId())
                .borrowDate(borrowDto.getBorrowDate())
                .borrowTimePeriodDays(borrowDto.getBorrowTimePeriodDays())
                .returnDate(borrowDto.getReturnDate())
                .borrowStatus(borrowDto.getBorrowStatus())
                .discountPercent(borrowDto.getDiscountPercent())
                .cost(borrowDto.getCost())
                .build();
    }
}
