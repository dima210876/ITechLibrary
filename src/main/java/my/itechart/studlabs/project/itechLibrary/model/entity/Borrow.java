package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Borrow
{
    private final Long id;
    private final Long readerId;
    private final LocalDate borrowDate;
    private final Integer borrowTimePeriodDays;
    private final LocalDate returnDate;
    private final String borrowStatus;
    private final Integer discountPercent;
    private final Double cost;

    public Borrow(Long id, Long readerId, LocalDate borrowDate, Integer borrowTimePeriodDays,
                  LocalDate returnDate, String borrowStatus, Integer discountPercent, Double cost)
    {
        this.id = id;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.borrowTimePeriodDays = borrowTimePeriodDays;
        this.returnDate = returnDate;
        this.borrowStatus = borrowStatus;
        this.discountPercent = discountPercent;
        this.cost = cost;
    }

    public static class BorrowBuilder
    {
        private Long id;
        private Long readerId;
        private LocalDate borrowDate;
        private Integer borrowTimePeriodDays;
        private LocalDate returnDate;
        private String borrowStatus;
        private Integer discountPercent;
        private Double cost;

        public BorrowBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public BorrowBuilder readerId(Long readerId)
        {
            this.readerId = readerId;
            return this;
        }

        public BorrowBuilder borrowDate(LocalDate borrowDate)
        {
            this.borrowDate = borrowDate;
            return this;
        }

        public BorrowBuilder borrowTimePeriodDays(Integer borrowTimePeriodDays)
        {
            this.borrowTimePeriodDays = borrowTimePeriodDays;
            return this;
        }

        public BorrowBuilder returnDate(LocalDate returnDate)
        {
            this.returnDate = returnDate;
            return this;
        }

        public BorrowBuilder borrowStatus(String borrowStatus)
        {
            this.borrowStatus = borrowStatus;
            return this;
        }

        public BorrowBuilder discountPercent(Integer discountPercent)
        {
            this.discountPercent = discountPercent;
            return this;
        }

        public BorrowBuilder cost(Double cost)
        {
            this.cost = cost;
            return this;
        }

        public Borrow build()
        {
            return new Borrow(
                    this.id,
                    this.readerId,
                    this.borrowDate,
                    this.borrowTimePeriodDays,
                    this.returnDate,
                    this.borrowStatus,
                    this.discountPercent,
                    this.cost
            );
        }
    }

    public static BorrowBuilder builder() { return new BorrowBuilder(); }

    public Long getId() { return id; }

    public Long getReaderId() { return readerId; }

    public LocalDate getBorrowDate() { return borrowDate; }

    public Integer getBorrowTimePeriodDays() { return borrowTimePeriodDays; }

    public LocalDate getReturnDate() { return returnDate; }

    public String getBorrowStatus() { return borrowStatus; }

    public Integer getDiscountPercent() { return discountPercent; }

    public Double getCost() { return cost; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrow borrow = (Borrow) o;
        return Objects.equals(id, borrow.id) && Objects.equals(readerId, borrow.readerId) &&
                Objects.equals(borrowDate, borrow.borrowDate) &&
                Objects.equals(borrowTimePeriodDays, borrow.borrowTimePeriodDays) &&
                Objects.equals(returnDate, borrow.returnDate) && Objects.equals(borrowStatus, borrow.borrowStatus) &&
                Objects.equals(discountPercent, borrow.discountPercent) && Objects.equals(cost, borrow.cost);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, readerId, borrowDate, borrowTimePeriodDays,
                returnDate, borrowStatus, discountPercent, cost);
    }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", readerId: " + readerId +
                ", borrowDate: " + borrowDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", borrowTimePeriodDays: " + borrowTimePeriodDays +
                ", returnDate: " + returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", borrowStatus: '" + borrowStatus + '\'' +
                ", discountPercent: " + discountPercent +
                ", cost: " + cost +
                '}';
    }
}
