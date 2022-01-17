package my.itechart.studlabs.project.itechLibrary.model.dto;

import my.itechart.studlabs.project.itechLibrary.model.entity.Reader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BorrowDto
{
    private final Long id;
    private final ReaderDto reader;
    private final LocalDate borrowDate;
    private final Integer borrowTimePeriodDays;
    private final LocalDate returnDate;
    private final String borrowStatus;
    private final Integer discountPercent;
    private final Double cost;
    private final List<BorrowRecordDto> records;

    public BorrowDto(Long id, ReaderDto reader, LocalDate borrowDate, Integer borrowTimePeriodDays, LocalDate returnDate,
                     String borrowStatus, Integer discountPercent, Double cost, List<BorrowRecordDto> records)
    {
        this.id = id;
        this.reader = reader;
        this.borrowDate = borrowDate;
        this.borrowTimePeriodDays = borrowTimePeriodDays;
        this.returnDate = returnDate;
        this.borrowStatus = borrowStatus;
        this.discountPercent = discountPercent;
        this.cost = cost;
        this.records = records;
    }

    public Long getId() { return id; }

    public ReaderDto getReader() { return reader; }

    public LocalDate getBorrowDate() { return borrowDate; }

    public Integer getBorrowTimePeriodDays() { return borrowTimePeriodDays; }

    public LocalDate getReturnDate() { return returnDate; }

    public String getBorrowStatus() { return borrowStatus; }

    public Integer getDiscountPercent() { return discountPercent; }

    public Double getCost() { return cost; }

    public List<BorrowRecordDto> getRecords() { return records; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowDto borrowDto = (BorrowDto) o;
        return Objects.equals(id, borrowDto.id) && Objects.equals(reader, borrowDto.reader) &&
                Objects.equals(borrowDate, borrowDto.borrowDate) &&
                Objects.equals(borrowTimePeriodDays, borrowDto.borrowTimePeriodDays) &&
                Objects.equals(returnDate, borrowDto.returnDate) &&
                Objects.equals(borrowStatus, borrowDto.borrowStatus) &&
                Objects.equals(discountPercent, borrowDto.discountPercent) &&
                Objects.equals(cost, borrowDto.cost) && Objects.equals(records, borrowDto.records);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, reader, borrowDate, borrowTimePeriodDays,
                returnDate, borrowStatus, discountPercent, cost, records);
    }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", reader: " + reader.toString() +
                ", borrowDate: " + borrowDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", borrowTimePeriodDays: " + borrowTimePeriodDays +
                ", returnDate: " + returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", borrowStatus: '" + borrowStatus + '\'' +
                ", discountPercent: " + discountPercent +
                ", cost: " + cost +
                ", records: " + Arrays.toString(records.toArray()) +
                '}';
    }
}
