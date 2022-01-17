package my.itechart.studlabs.project.itechLibrary.model.factory;

import my.itechart.studlabs.project.itechLibrary.model.dto.BookDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookFactory
{
    private static final BookFactory INSTANCE = new BookFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String TITLE_RU_COLUMN_NAME = "title_ru";
    private static final String TITLE_ORIGIN_COLUMN_NAME = "title_origin";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String BOOK_COST_COLUMN_NAME = "book_cost";
    private static final String DAY_COST_COLUMN_NAME = "day_cost";
    private static final String EDITION_YEAR_COLUMN_NAME = "edition_year";
    private static final String PAGE_COUNT_COLUMN_NAME = "page_count";
    private static final String REGISTRATION_DATE_COLUMN_NAME = "registration_date";

    private BookFactory() { }

    public static BookFactory getInstance() { return INSTANCE; }

    public Book create(ResultSet resultSet) throws SQLException
    {
        return Book.builder()
                .id(resultSet.getLong(ID_COLUMN_NAME))
                .titleRu(resultSet.getString(TITLE_RU_COLUMN_NAME))
                .titleOrigin(resultSet.getString(TITLE_ORIGIN_COLUMN_NAME))
                .description(resultSet.getString(DESCRIPTION_COLUMN_NAME))
                .bookCost(resultSet.getDouble(BOOK_COST_COLUMN_NAME))
                .dayCost(resultSet.getDouble(DAY_COST_COLUMN_NAME))
                .editionYear(resultSet.getInt(EDITION_YEAR_COLUMN_NAME))
                .pageCount(resultSet.getInt(PAGE_COUNT_COLUMN_NAME))
                .registrationDate(resultSet.getDate(REGISTRATION_DATE_COLUMN_NAME).toLocalDate())
                .build();
    }

    public Book create(BookDto bookDto)
    {
        return Book.builder()
                .id(bookDto.getId())
                .titleRu(bookDto.getTitleRu())
                .titleOrigin(bookDto.getTitleOrigin())
                .description(bookDto.getDescription())
                .bookCost(bookDto.getBookCost())
                .dayCost(bookDto.getDayCost())
                .editionYear(bookDto.getEditionYear())
                .pageCount(bookDto.getPageCount())
                .registrationDate(bookDto.getRegistrationDate())
                .build();
    }
}
