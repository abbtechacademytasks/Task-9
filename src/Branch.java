import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
    private static int nextId = 0;
    private String branchId;
    private final String name;
    private final String address;

    Map<String, List<BookCopy>> bookCopies =  new HashMap<>();

    public Branch(String name, String address) {
        this.branchId = "B-" + nextId++;
        this.name = name;
        this.address = address;
    }
}
