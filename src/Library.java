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

    private final Comparator<Reservation> reservationComparator = (r1, r2) -> {
        int priorityComparison = Integer.compare(
                r1.getPriorityScore(),
                r2.getPriorityScore()
        );

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return Integer.compare(
                r1.getReservationDay(),
                r2.getReservationDay()
        );
    };

    boolean borrowBook(Member m, String bookId, String branchId, int currentDay) {
        if (blackList.contains(m)) {
            printMessage("Member is blacklisted!");
            return false;
        }

        Branch branch = branches.get(branchId);

        if (branch == null) {
            printMessage("Branch not found!");
            return false;
        }

        List<BookCopy> copies = branch.bookCopies.get(bookId);

        if (copies != null) {
            for (BookCopy copy : copies) {
                if (copy.getStatus() == CopyStatus.AVAILABLE) {
                    copy.setStatus(CopyStatus.BORROWED);
                    int dueDay = currentDay;

                    if (m.getMembershipType() == MembershipType.PREMIUM) {
                        dueDay += 21;
                    } else {
                        dueDay += 14;
                    }

                    Loan loan = new Loan(m, copy, currentDay, dueDay);

                    memberLoans.computeIfAbsent(m, k -> new ArrayList<>());
                    memberLoans.get(m).add(loan);

                    loanHistory.add(loan);

                    bookInterestsStats.put(copy.getBook(),
                            bookInterestsStats.getOrDefault(copy.getBook(), 0) + 1);

                    activeMembers.put(m, activeMembers.getOrDefault(m, 0) + 1);

                    return true;
                }
            }
        }

        Book requestedBook = null;

        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                requestedBook = book;
                break;
            }
        }

        if  (requestedBook == null) {
            printMessage("Book not found!");
            return false;
        }

        PriorityQueue<Reservation> queue = reservations.get(bookId);

        if (queue == null) {
            queue = new PriorityQueue<>(reservationComparator);
            reservations.put(bookId, queue);
        }

        Reservation reservation = new Reservation(m, requestedBook, currentDay, m.getMembershipType().getValue());

        queue.offer(reservation);
        printMessage("Book not available, added to reservation queue");

        return false;
    }

    void returnBook(Member m, String loanId, int currentDay) {
        //todo


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

    void printMessage(String message) {
        System.out.println(message);
    }
}

