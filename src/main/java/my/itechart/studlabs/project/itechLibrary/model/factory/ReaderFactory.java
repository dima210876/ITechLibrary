package my.itechart.studlabs.project.itechLibrary.model.factory;

import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.Reader;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderFactory
{
    private static final ReaderFactory INSTANCE = new ReaderFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String FIRST_NAME_COLUMN_NAME = "first_name";
    private static final String LAST_NAME_COLUMN_NAME = "last_name";
    private static final String MIDDLE_NAME_COLUMN_NAME = "middle_name";
    private static final String PASSPORT_NUMBER_COLUMN_NAME = "passport_number";
    private static final String BIRTH_DATE_COLUMN_NAME = "birth_date";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String ADDRESS_COLUMN_NAME = "address";

    private ReaderFactory() { }

    public static ReaderFactory getInstance() { return INSTANCE; }

    public Reader create(ResultSet resultSet) throws SQLException
    {
        return Reader.builder()
                .id(resultSet.getLong(ID_COLUMN_NAME))
                .firstName(resultSet.getString(FIRST_NAME_COLUMN_NAME))
                .lastName(resultSet.getString(LAST_NAME_COLUMN_NAME))
                .middleName(resultSet.getString(MIDDLE_NAME_COLUMN_NAME))
                .passportNumber(resultSet.getString(PASSPORT_NUMBER_COLUMN_NAME))
                .birthDate(resultSet.getDate(BIRTH_DATE_COLUMN_NAME).toLocalDate())
                .email(resultSet.getString(EMAIL_COLUMN_NAME))
                .address(resultSet.getString(ADDRESS_COLUMN_NAME))
                .build();
    }

    public Reader create(ReaderDto readerDto)
    {
        return Reader.builder()
                .id(readerDto.getId())
                .firstName(readerDto.getFirstName())
                .lastName(readerDto.getLastName())
                .middleName(readerDto.getMiddleName())
                .passportNumber(readerDto.getPassportNumber())
                .birthDate(readerDto.getBirthDate())
                .email(readerDto.getEmail())
                .address(readerDto.getAddress())
                .build();
    }
}
