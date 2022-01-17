package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.util.Objects;

public class Genre
{
    private final Long id;
    private final String genreName;

    public Genre(Long id, String genreName)
    {
        this.id = id;
        this.genreName = genreName;
    }

    public static class GenreBuilder
    {
        private Long id;
        private String genreName;

        public GenreBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public GenreBuilder genreName(String genreName)
        {
            this.genreName = genreName;
            return this;
        }

        public Genre build()
        {
            return new Genre(this.id, this.genreName);
        }
    }

    public static GenreBuilder builder() { return new GenreBuilder(); }

    public Long getId() { return id; }

    public String getGenreName() { return genreName; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) && Objects.equals(genreName, genre.genreName);
    }

    @Override
    public int hashCode() { return Objects.hash(id, genreName); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", genreName: '" + genreName + '\'' +
                '}';
    }
}
