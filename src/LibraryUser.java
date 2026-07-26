abstract class LibraryUser {
    String id;
    String name;
    String email;

    public LibraryUser(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    abstract double getDiscountRate();
}
