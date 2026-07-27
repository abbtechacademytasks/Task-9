public class Notification {
    private final NotificationType type;
    private final String message;
    private final int day;

    public Notification(NotificationType type, String message, int day) {
        this.type = type;
        this.message = message;
        this.day = day;
    }

    NotificationType getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    int getDay() {
        return day;
    }
}
