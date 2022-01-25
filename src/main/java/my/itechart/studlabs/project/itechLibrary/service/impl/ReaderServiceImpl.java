package my.itechart.studlabs.project.itechLibrary.service.impl;

import my.itechart.studlabs.project.itechLibrary.dao.factory.defaultDaoFactory.*;
import my.itechart.studlabs.project.itechLibrary.dao.impl.defaultDao.*;
import my.itechart.studlabs.project.itechLibrary.error.TransactionException;
import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.*;
import my.itechart.studlabs.project.itechLibrary.model.factory.ReaderFactory;
import my.itechart.studlabs.project.itechLibrary.service.ReaderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReaderServiceImpl implements ReaderService
{
    private static final ReaderServiceImpl INSTANCE = new ReaderServiceImpl();
    private static final Logger LOGGER = LogManager.getLogger(ReaderServiceImpl.class);

    private final ReaderDao readerDao;

    public ReaderServiceImpl()
    {
        this.readerDao = ReaderDaoFactory.getInstance().getDao();
    }

    public static ReaderServiceImpl getInstance() { return INSTANCE; }

    @Override
    public List<ReaderDto> findAll()
    {
        return checkOptionalList(
                readerDao.findAll()
                        .map(readers -> readers.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<ReaderDto> findBySortingAndPage(String sortingColumn, int page)
    {
        return checkOptionalList(
                readerDao.findBySortingAndPageNumber(sortingColumn, page)
                        .map(readers -> readers.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<ReaderDto> findByReversedSortingAndPage(String sortingColumn, int page)
    {
        return checkOptionalList(
                readerDao.findByReversedSortingAndPageNumber(sortingColumn, page)
                        .map(readers -> readers.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public int getCountOfPages() { return readerDao.getCountOfPages(); }

    @Override
    public ReaderDto findById(long id)
    {
        return readerDao.findById(id).map(this::convertToDto).orElse(null);
    }

    @Override
    public ReaderDto create(ReaderDto readerDto)
    {
        ReaderDto createdReader = null;
        try
        {
            Reader newReader = ReaderFactory.getInstance().create(readerDto);
            Optional<Reader> reader = readerDao.create(newReader);
            if (reader.isEmpty()) { throw new TransactionException("Can't create a new reader"); }
            createdReader = reader.map(this::convertToDto).orElse(null);
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to create a new reader: " + e.getLocalizedMessage());
        }
        return createdReader;
    }

    @Override
    public ReaderDto update(ReaderDto readerDto)
    {
        ReaderDto updatedReader = null;
        try
        {
            readerDao.update(ReaderFactory.getInstance().create(readerDto))
                    .orElseThrow(() -> new TransactionException("Can't update a reader"));
            updatedReader = readerDao.findById(readerDto.getId()).map(this::convertToDto).orElse(null);
        }
        catch (TransactionException e)
        {
            LOGGER.error("Exception while trying to update reader with id = " + readerDto.getId() +
                    ": " + e.getLocalizedMessage());
        }
        return updatedReader;
    }

    @Override
    public ReaderDto findReaderByEmail(String email)
    {
        return readerDao.findByEmail(email).map(this::convertToDto).orElse(null);
    }

    private ReaderDto convertToDto(Reader reader)
    {
        return new ReaderDto(reader.getId(), reader.getFirstName(), reader.getLastName(), reader.getMiddleName(),
                reader.getPassportNumber(), reader.getBirthDate(), reader.getEmail(), reader.getAddress());
    }
}
