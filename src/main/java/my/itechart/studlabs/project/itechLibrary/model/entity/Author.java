package my.itechart.studlabs.project.itechLibrary.model.entity;

import java.util.Objects;

public class Author
{
    private final Long id;
    private final String name;
    private final String surname;
    private final String photoPath;

    public Author(Long id, String name, String surname, String photoPath)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.photoPath = photoPath;
    }

    public static class AuthorBuilder
    {
        private Long id;
        private String name;
        private String surname;
        private String photoPath;

        public AuthorBuilder id(Long id)
        {
            this.id = id;
            return this;
        }

        public AuthorBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public AuthorBuilder surname(String surname)
        {
            this.surname = surname;
            return this;
        }

        public AuthorBuilder photoPath(String photoPath)
        {
            this.photoPath = photoPath;
            return this;
        }

        public Author build()
        {
            return new Author(this.id, this.name, this.surname, this.photoPath);
        }
    }

    public static AuthorBuilder builder() { return new AuthorBuilder(); }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getPhotoPath() { return photoPath; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(name, author.name) &&
                Objects.equals(surname, author.surname) && Objects.equals(photoPath, author.photoPath);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, surname, photoPath); }

    @Override
    public String toString()
    {
        return "{" +
                "id: " + id +
                ", name: '" + name + '\'' +
                ", surname: '" + surname + '\'' +
                ", photoPath: '" + photoPath + '\'' +
                '}';
    }
}
