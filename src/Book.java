import java.util.Objects;

public class Book {
    private static int nextId = 0;
    private String id;
    private final String title;
    private final String author;
    private final String genre;
    private final String isbn;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre) && Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, genre, isbn);
    }

    public Book(String title, String author, String genre, String isbn) {
        this.id = "B-" + nextId++;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
    }

    String getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getGenre() {
        return genre;
    }
}
