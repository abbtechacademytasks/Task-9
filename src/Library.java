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
        if (isMemberContainInBlackList(m)) {
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
        Loan searchedLoan = null;

        for (Loan loan : loanHistory) {
            if (loan.getLoanId().equals(loanId)) {
                searchedLoan = loan;
                break;
            }
        }

        if (searchedLoan == null) {
            printMessage("Loan not found!");
            return;
        }

        if (!searchedLoan.getMember().equals(m)) {
            printMessage("Member not associated with the loan!");
            return;
        }

        if (searchedLoan.isReturned()) {
            printMessage("Loan already returned!");
            return;
        }

        searchedLoan.setReturned(true);

        List<Loan> activeLoans = memberLoans.get(m);

        if (activeLoans != null) {
            activeLoans.remove(searchedLoan);

            if (activeLoans.isEmpty()) {
                memberLoans.remove(m);
            }
        }

        if (currentDay > searchedLoan.getDueDay()) {
            int daysLate = currentDay - searchedLoan.getDueDay();
            FineRecord fineRecord = new FineRecord(m.calculateFine(daysLate), m,
                    "Return date overdue.", currentDay);
            fineRecords.add(fineRecord);
        }

        int counter = 0;
        for (FineRecord fineRecord : fineRecords) {
            if (fineRecord.getMember().equals(m)) {
                counter++;
            }
        }

        if (counter > 3 && blackList.add(m)) {
            Notification notification = new Notification(NotificationType.BLACKLIST_WARNING,
                    "You are added to black list.", currentDay);

            Queue<Notification> notifications = memberNotifications.computeIfAbsent(m, k -> new ArrayDeque<>());
            notifications.offer(notification);
        }

        PriorityQueue<Reservation> queue = reservations.get(searchedLoan.getBookCopy().getBook().getId());

        searchedLoan.getBookCopy().setStatus(CopyStatus.AVAILABLE);

        if (queue != null && !queue.isEmpty()) {
            boolean isFound = false;

            while (!queue.isEmpty() && !isFound) {
                Reservation reservation = queue.poll();

                if (takeBook(reservation.getMember(), searchedLoan.getBookCopy(), currentDay)) {
                    Notification notification = new Notification(NotificationType.RESERVATION_READY,
                            "Your reservation are ready.", currentDay);

                    Queue<Notification> notifications =
                            memberNotifications.computeIfAbsent(reservation.getMember(), k -> new ArrayDeque<>());
                    notifications.offer(notification);
                    isFound = true;
                }
            }
        }
    }

    boolean transferBook(String bookId, String fromBranchId, String toBranchId) {
        Branch fromBranch = branches.get(fromBranchId);
        Branch toBranch = branches.get(toBranchId);

        if (fromBranch == null || toBranch == null) {
            return false;
        }

        List<BookCopy> bookCopies = fromBranch.bookCopies.get(bookId);
        if (bookCopies == null || bookCopies.isEmpty()) {
            printMessage("Transfer failed: book not found in source branch");
            return false;
        }

        BookCopy transferringCopy = null;

        for (BookCopy bookCopy : bookCopies) {
            if (bookCopy.getStatus() == CopyStatus.AVAILABLE) {
                transferringCopy = bookCopy;
                break;
            }
        }

        if (transferringCopy == null) {
            printMessage("Transfer failed: book not found in source branch");
            return false;
        }

        transferringCopy.setStatus(CopyStatus.IN_TRANSIT);
        bookCopies.remove(transferringCopy);
        transferringCopy.setBranchId(toBranchId);

        toBranch.bookCopies
                .computeIfAbsent(bookId, key -> new ArrayList<>())
                .add(transferringCopy);

        transferringCopy.setStatus(CopyStatus.AVAILABLE);

        transferHistory.offerFirst("Book " + transferringCopy.getBook().getTitle() + ": " +
                fromBranch.getName() + " -> " + toBranch.getName());

        if (transferHistory.size() > 5) {
            transferHistory.pollLast();
        }

        return true;
    }

    List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        Set<String> foundBookIds = new HashSet<>();
        keyword = keyword.trim().toLowerCase();

        for (Branch branch : branches.values()) {
            for (List<BookCopy> bookCopies : branch.bookCopies.values()) {
                for (BookCopy bookCopy : bookCopies) {
                    Book book = bookCopy.getBook();

                    if  (book.getTitle().toLowerCase().contains(keyword) ||
                            book.getAuthor().toLowerCase().contains(keyword) ||
                            book.getGenre().toLowerCase().contains(keyword)) {
                        if (foundBookIds.add(book.getId())) {
                            result.add(book);
                        }
                    }
                }
            }
        }

        return result;
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

    boolean isMemberContainInBlackList(Member member) {
        return blackList.contains(member);
    }

    boolean takeBook(Member m, BookCopy bookCopy, int currentDay) {
        if (isMemberContainInBlackList(m)) {
            printMessage("Member is blacklisted!");
            return false;
        }

        if (bookCopy.getStatus() == CopyStatus.AVAILABLE) {
            bookCopy.setStatus(CopyStatus.BORROWED);
            int dueDay = currentDay;

            if (m.getMembershipType() == MembershipType.PREMIUM) {
                dueDay += 21;
            } else {
                dueDay += 14;
            }

            Loan loan = new Loan(m, bookCopy, currentDay, dueDay);

            memberLoans.computeIfAbsent(m, k -> new ArrayList<>());
            memberLoans.get(m).add(loan);

            loanHistory.add(loan);

            bookInterestsStats.put(bookCopy.getBook(),
                    bookInterestsStats.getOrDefault(bookCopy.getBook(), 0) + 1);

            activeMembers.put(m, activeMembers.getOrDefault(m, 0) + 1);

            return true;
        }

        return false;
    }
}

