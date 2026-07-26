public class Loan {
    private static int nextId = 0;
    private final String loanId;
    private final BookCopy bookCopy;
    private final Member member;
    private final int borrowDay;
    private final int dueDay;
    private boolean returned;

    public Loan(Member member, BookCopy bookCopy, int borrowDay, int dueDay) {
        this.loanId = "L-" + nextId++;
        this.member = member;
        this.bookCopy = bookCopy;
        this.borrowDay = borrowDay;
        this.dueDay = dueDay;
        this.returned = false;
    }

    String getLoanId() {
        return loanId;
    }

    Boolean isReturned() {
        return returned;
    }

    void setReturned(boolean returned) {
        this.returned = returned;
    }

    Member getMember() {
        return member;
    }

    int getDueDay() {
        return dueDay;
    }

    BookCopy getBookCopy() {
        return bookCopy;
    }
}
