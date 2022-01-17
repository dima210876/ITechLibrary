package my.itechart.studlabs.project.itechLibrary.service;

import my.itechart.studlabs.project.itechLibrary.model.dto.BorrowDto;
import my.itechart.studlabs.project.itechLibrary.model.entity.Borrow;

import java.util.List;

public interface BorrowService extends DefaultService<BorrowDto>
{
    List<BorrowDto> findAllReaderBorrows(long readerId);

    List<BorrowDto> findNotReturnedReaderBorrows(long readerId);

    List<BorrowDto> findExpiredBorrows();

    boolean updateBorrowStatus();

    boolean returnBorrow(BorrowDto borrowDto);
}
