import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {
    String branchId;
    String name;
    String address;

    Map<String, List<BookCopy>> bookCopies =  new HashMap<>();
}
