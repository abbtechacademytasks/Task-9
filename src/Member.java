import java.util.Objects;

public class Member extends LibraryUser implements Finable, Notifiable {
    private static int nextId = 0;
    private final MembershipType membershipType;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

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
        double fineRate;

        switch (membershipType) {
            case STUDENT ->  fineRate = 0.3;
            case REGULAR ->   fineRate = 0.5;
            case PREMIUM ->   fineRate = 0.2;
            default -> fineRate = 1;
        }

        return fineRate *  daysLate;
    }

    @Override
    public void receiveNotification(Notification n) {

    }

    MembershipType getMembershipType() {
        return membershipType;
    }
}
