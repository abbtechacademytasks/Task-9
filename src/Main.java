import java.util.List;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Branch centralBranch = new Branch("Central Branch", "28 May Street");
        Branch cityBranch = new Branch("City Branch", "Nizami Street");

        library.addBranch(centralBranch);
        library.addBranch(cityBranch);

        Book effectiveJava = new Book(
                "Effective Java", "Joshua Bloch", "Programming", "978-0134685991"
        );
        Book cleanCode = new Book(
                "Clean Code", "Robert Martin", "Programming", "978-0132350884"
        );
        Book javaConcurrency = new Book(
                "Java Concurrency in Practice", "Brian Goetz", "Programming", "978-0321349606"
        );
        Book designPatterns = new Book(
                "Design Patterns", "Erich Gamma", "Software Design", "978-0201633610"
        );
        Book refactoring = new Book(
                "Refactoring", "Martin Fowler", "Software Design", "978-0134757599"
        );
        Book algorithms = new Book(
                "Introduction to Algorithms", "Thomas Cormen", "Algorithms", "978-0262046305"
        );

        library.addBook(effectiveJava);
        library.addBook(cleanCode);
        library.addBook(javaConcurrency);
        library.addBook(designPatterns);
        library.addBook(refactoring);
        library.addBook(algorithms);

        centralBranch.addBookCopy(
                new BookCopy(effectiveJava, centralBranch.getBranchId(), CopyStatus.AVAILABLE)
        );
        centralBranch.addBookCopy(
                new BookCopy(cleanCode, centralBranch.getBranchId(), CopyStatus.AVAILABLE)
        );
        centralBranch.addBookCopy(
                new BookCopy(javaConcurrency, centralBranch.getBranchId(), CopyStatus.AVAILABLE)
        );
        centralBranch.addBookCopy(
                new BookCopy(designPatterns, centralBranch.getBranchId(), CopyStatus.AVAILABLE)
        );
        centralBranch.addBookCopy(
                new BookCopy(refactoring, centralBranch.getBranchId(), CopyStatus.AVAILABLE)
        );
        centralBranch.addBookCopy(
                new BookCopy(algorithms, centralBranch.getBranchId(), CopyStatus.AVAILABLE)
        );

        Member regularMember = new Member(
                "Ali", "ali@example.com", MembershipType.REGULAR
        );
        Member premiumMember = new Member(
                "Leyla", "leyla@example.com", MembershipType.PREMIUM
        );
        Member studentMember = new Member(
                "Murad", "murad@example.com", MembershipType.STUDENT
        );
        Member lateMember = new Member(
                "Nigar", "nigar@example.com", MembershipType.REGULAR
        );

        System.out.println("=== Borrow and priority reservation ===");

        library.borrowBook(
                regularMember,
                effectiveJava.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );

        String regularLoanId =
                library.memberLoans.get(regularMember).get(0).getLoanId();

        library.borrowBook(
                studentMember,
                effectiveJava.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );
        library.borrowBook(
                premiumMember,
                effectiveJava.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );

        library.borrowBook(
                lateMember,
                cleanCode.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );
        library.borrowBook(
                lateMember,
                javaConcurrency.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );
        library.borrowBook(
                lateMember,
                designPatterns.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );
        library.borrowBook(
                lateMember,
                refactoring.getId(),
                centralBranch.getBranchId(),
                library.getCurrentDay()
        );

        List<Loan> lateLoans = library.memberLoans.get(lateMember);
        String[] lateLoanIds = new String[lateLoans.size()];

        for (int i = 0; i < lateLoans.size(); i++) {
            lateLoanIds[i] = lateLoans.get(i).getLoanId();
        }

        library.returnBook(
                regularMember,
                regularLoanId,
                library.getCurrentDay()
        );

        System.out.println("\n=== Transfer ===");
        library.transferBook(
                algorithms.getId(),
                centralBranch.getBranchId(),
                cityBranch.getBranchId()
        );
        System.out.println(library.transferHistory);

        System.out.println("\n=== Search ===");
        List<Book> searchResult = library.searchBooks("java");

        for (Book book : searchResult) {
            System.out.println(book.getTitle());
        }

        System.out.println("\n=== Report before overdue loans ===");
        library.generateBranchReport(centralBranch.getBranchId());

        for (int i = 0; i < 15; i++) {
            library.nextDay();
        }

        System.out.println("\nCurrent day: " + library.getCurrentDay());
        System.out.println("=== Report with overdue loans ===");
        library.generateBranchReport(centralBranch.getBranchId());

        System.out.println("\n=== Late returns and blacklist ===");

        for (String loanId : lateLoanIds) {
            library.returnBook(
                    lateMember,
                    loanId,
                    library.getCurrentDay()
            );
        }

        library.borrowBook(
                lateMember,
                algorithms.getId(),
                cityBranch.getBranchId(),
                library.getCurrentDay()
        );

        System.out.println("\n=== Top active members ===");
        List<Member> topMembers = library.getTopActiveMembers(3);

        for (Member member : topMembers) {
            System.out.println(
                    member.name + ": " + library.activeMembers.get(member) + " loan(s)"
            );
        }

        System.out.println("\n=== Notifications ===");
        library.processNotifications(library.getCurrentDay());

        System.out.println("\n=== Final report ===");
        library.generateBranchReport(centralBranch.getBranchId());
    }
}
