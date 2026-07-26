public class Member extends LibraryUser implements Finable, Notifiable {
    private static int nextId = 0;
    private final MembershipType membershipType;

    public Member(String name, String email, MembershipType membershipType) {
        super("M-" + nextId++, name, email);
        this.membershipType = membershipType;
    }


    @Override
    double getDiscountRate() {
        return 0;
    }

    @Override
    public double calculateFine(int daysLate) {
        return 0;
    }

    @Override
    public void receiveNotification(Notification n) {

    }

    MembershipType getMembershipType() {
        return membershipType;
    }
}
