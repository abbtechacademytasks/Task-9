public class Reservation {
    private final Member member;
    private final Book book;
    private final int reservationDay;
    private final int priorityScore;

    public Reservation(Member member, Book book, int reservationDay, int priorityScore) {
        this.member = member;
        this.book = book;
        this.reservationDay = reservationDay;
        this.priorityScore = priorityScore;
    }

    int getReservationDay() {
        return reservationDay;
    }

    int getPriorityScore() {
        return priorityScore;
    }
}
