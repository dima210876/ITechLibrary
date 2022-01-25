package my.itechart.studlabs.project.itechLibrary.service;

import my.itechart.studlabs.project.itechLibrary.model.dto.ReaderDto;

import java.util.List;

public interface ReaderService extends DefaultService<ReaderDto>
{
    ReaderDto findReaderByEmail(String email);

    List<ReaderDto> findBySortingAndPage(String sortingColumn, int page);

    List<ReaderDto> findByReversedSortingAndPage(String sortingColumn, int page);
}
