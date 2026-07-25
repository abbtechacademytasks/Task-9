public enum MembershipType {
    PREMIUM(1),
    REGULAR(2),
    STUDENT(3);

    private final int value;
    MembershipType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
