package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Book
{
    private final Long id;
    private final String titleRu;
    private final String titleOrigin;
    private final String description;
    private final Double bookCost;
    private final Double dayCost;
    private final Integer editionYear;
    private final Integer pageCount;
    private final LocalDate registrationDate;

    public Book(Long id, String titleRu, String titleOrigin, String description, Double bookCost, Double dayCost,
                Integer editionYear, Integer pageCount, LocalDate registrationDate)
    {
        this.id = id;
        this.titleRu = titleRu;
        this.titleOrigin = titleOrigin;
        this.description = description;
        this.bookCost = bookCost;
        this.dayCost = dayCost;
        this.editionYear = editionYear;
        this.pageCount = pageCount;
        this.registrationDate = registrationDate;
    }

    public static class BookBuilder
    {
        private Long id;
        private String titleRu;
        private String titleOrigin;
        private String description;
        private Double bookCost;
        private Double dayCost;
        private Integer editionYear;
        private Integer pageCount;
        private LocalDate registrationDate;

        public BookBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public BookBuilder titleRu(String titleRu)
        {
            this.titleRu = titleRu;
            return this;
        }

        public BookBuilder titleOrigin(String titleOrigin)
        {
            this.titleOrigin = titleOrigin;
            return this;
        }

        public BookBuilder description(String description)
        {
            this.description = description;
            return this;
        }

        public BookBuilder bookCost(Double bookCost)
        {
            this.bookCost = bookCost;
            return this;
        }

        public BookBuilder dayCost(Double dayCost)
        {
            this.dayCost = dayCost;
            return this;
        }

        public BookBuilder editionYear(Integer editionYear)
        {
            this.editionYear = editionYear;
            return this;
        }

        public BookBuilder pageCount(Integer pageCount)
        {
            this.pageCount = pageCount;
            return this;
        }

        public BookBuilder registrationDate(LocalDate registrationDate)
        {
            this.registrationDate = registrationDate;
            return this;
        }

        public Book build()
        {
            return new Book(
                    this.id,
                    this.titleRu,
                    this.titleOrigin,
                    this.description,
                    this.bookCost,
                    this.dayCost,
                    this.editionYear,
                    this.pageCount,
                    this.registrationDate
            );
        }
    }

    public static BookBuilder builder() { return new BookBuilder(); }

    public Long getId() { return id; }

    public String getTitleRu() { return titleRu; }

    public String getTitleOrigin() { return titleOrigin; }

    public String getDescription() { return description; }

    public Double getBookCost() { return bookCost; }

    public Double getDayCost() { return dayCost; }

    public Integer getEditionYear() { return editionYear; }

    public Integer getPageCount() { return pageCount; }

    public LocalDate getRegistrationDate() { return registrationDate; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(titleRu, book.titleRu) &&
                Objects.equals(titleOrigin, book.titleOrigin) && Objects.equals(description, book.description) &&
                Objects.equals(bookCost, book.bookCost) && Objects.equals(dayCost, book.dayCost) &&
                Objects.equals(editionYear, book.editionYear) && Objects.equals(pageCount, book.pageCount) &&
                Objects.equals(registrationDate, book.registrationDate);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, titleRu, titleOrigin, description, bookCost,
                dayCost, editionYear, pageCount, registrationDate);
    }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", titleRu: '" + titleRu + '\'' +
                ", titleOrigin: '" + titleOrigin + '\'' +
                ", description: '" + description + '\'' +
                ", bookCost: " + bookCost +
                ", dayCost: " + dayCost +
                ", editionYear: " + editionYear +
                ", pageCount: " + pageCount +
                ", registrationDate: " + registrationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                '}';
    }
}
