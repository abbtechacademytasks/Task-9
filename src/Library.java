import java.util.*;

public class Library {
    Map<String, Branch> branches =  new HashMap<>();
    Set<Book> books = new HashSet<>();
    Map<Member, List<Loan>>  memberLoans =  new HashMap<>();
    List<Loan> loanHistory =  new ArrayList<>();
    Map<String, PriorityQueue<Reservation>>  reservations =   new HashMap<>();
    Map<Book, Integer> bookInterestsStats =   new HashMap<>();
    List<FineRecord> fineRecords =  new ArrayList<>();
    Map<Member, Queue<Notification>>  memberNotifications  =  new HashMap<>();
    Set<Member> blackList =   new HashSet<>();
    Deque<String> transferHistory = new ArrayDeque<>();
    TreeMap<String, Set<Book>> bookGenres =  new TreeMap<>();
    Map<Member, Integer> activeMembers =  new HashMap<>();

    boolean borrowBook(Member m, String bookId, String branchId, int currentDay) {
        return true; // todo
    }

    void returnBook(Member m, String loanId, int currentDay) {
        // todo
    }

    boolean transferBook(String bookId, String fromBranchId, String toBranchId) {
        return true; // todo
    }

    List<Book> searchBooks(String keyword) {
        return null; // todo
    }

    void generateBranchReport(String branchId) {
        // todo
    }

    List<Member> getTopActiveMembers(int topN) {
        return null; // todo
    }

    void processNotifications(int currentDay) {
        // todo
    }
}

