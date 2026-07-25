public class Member extends LibraryUser implements Finable, Notifiable {
    MembershipType membershipType;

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
}
