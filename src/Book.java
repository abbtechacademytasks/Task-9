public class Book {
    private static int nextId = 0;
    private String id;
    private final String title;
    private final String author;
    private final String genre;
    private final String isbn;

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
}
