public class FineRecord {
    Member member;
    double amount;
    String reason;
    int day;

    public FineRecord(double amount, Member member, String reason, int day) {
        this.amount = amount;
        this.member = member;
        this.reason = reason;
        this.day = day;
    }

    Member getMember() {
        return member;
    }
}
