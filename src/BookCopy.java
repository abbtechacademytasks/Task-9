public class BookCopy {
    private static int nextId;
    private final String copyId;
    private final Book book;
    private String branchId;
    private CopyStatus status;

    public BookCopy(Book book, String branchId, CopyStatus status) {
        this.copyId = "C-" + nextId++;
        this.book = book;
        this.branchId = branchId;
        this.status = status;
    }

    CopyStatus getStatus() {
        return status;
    }

    void setStatus(CopyStatus status) {
        this.status = status;
    }

    Book getBook() {
        return book;
    }

    String getBranchId() {
        return branchId;
    }

    void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}
