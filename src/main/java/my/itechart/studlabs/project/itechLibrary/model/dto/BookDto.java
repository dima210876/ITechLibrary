package my.itechart.studlabs.project.itechLibrary.model.dto;

import my.itechart.studlabs.project.itechLibrary.model.entity.Author;
import my.itechart.studlabs.project.itechLibrary.model.entity.Genre;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookDto
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

    private final List<Author> authors;
    private final List<Genre> genres;
    private final List<String> coverPhotos;
    private final Integer totalCopyCount;
    private final Integer availableCopyCount;

    public BookDto(Long id, String titleRu, String titleOrigin, String description, Double bookCost, Double dayCost,
                   Integer editionYear, Integer pageCount, LocalDate registrationDate, List<Author> authors,
                   List<Genre> genres, List<String> coverPhotos, Integer totalCopyCount, Integer availableCopyCount)
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
        this.authors = authors;
        this.genres = genres;
        this.coverPhotos = coverPhotos;
        this.totalCopyCount = totalCopyCount;
        this.availableCopyCount = availableCopyCount;
    }

    public Long getId() { return id; }

    public String getTitleRu() { return titleRu; }

    public String getTitleOrigin() { return titleOrigin; }

    public String getDescription() { return description; }

    public Double getBookCost() { return bookCost; }

    public Double getDayCost() { return dayCost; }

    public Integer getEditionYear() { return editionYear; }

    public Integer getPageCount() { return pageCount; }

    public LocalDate getRegistrationDate() { return registrationDate; }

    public List<Author> getAuthors() { return authors; }

    public List<Genre> getGenres() { return genres; }

    public List<String> getCoverPhotos() { return coverPhotos; }

    public Integer getTotalCopyCount() { return totalCopyCount; }

    public Integer getAvailableCopyCount() { return availableCopyCount; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(id, bookDto.id) && Objects.equals(titleRu, bookDto.titleRu) &&
                Objects.equals(titleOrigin, bookDto.titleOrigin) && Objects.equals(description, bookDto.description) &&
                Objects.equals(bookCost, bookDto.bookCost) && Objects.equals(dayCost, bookDto.dayCost) &&
                Objects.equals(editionYear, bookDto.editionYear) && Objects.equals(pageCount, bookDto.pageCount) &&
                Objects.equals(registrationDate, bookDto.registrationDate) && Objects.equals(authors, bookDto.authors) &&
                Objects.equals(genres, bookDto.genres) && Objects.equals(coverPhotos, bookDto.coverPhotos) &&
                Objects.equals(totalCopyCount, bookDto.totalCopyCount) && Objects.equals(availableCopyCount, bookDto.availableCopyCount);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, titleRu, titleOrigin, description, bookCost, dayCost, editionYear,
                pageCount, registrationDate, authors, genres, coverPhotos, totalCopyCount, availableCopyCount);
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
                ", authors: " + Arrays.toString(authors.toArray()) +
                ", genres: " + Arrays.toString(genres.toArray()) +
                ", coverPhotos: " + Arrays.toString(coverPhotos.toArray()) +
                ", totalCopyCount: " + totalCopyCount +
                ", availableCopyCount: " + availableCopyCount +
                '}';
    }
}
