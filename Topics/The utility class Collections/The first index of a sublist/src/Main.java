import java.util.*;

class Main {
    private static Scanner scanner;
    
    public static void main(String[] args) {
        // put your code here
        
        scanner = new Scanner(System.in);
        
        List<Integer> first = getIntegerList();
        List<Integer> second = getIntegerList();
        
        int firstIndex = Collections.indexOfSubList(first, second);
        int lastIndex = Collections.lastIndexOfSubList(first, second);
        
        System.out.println(firstIndex + " " + lastIndex);
    }
    
    private static ArrayList<Integer> getIntegerList() {
        List<String> listStr = Arrays.stream(scanner.nextLine().split(" ")).toList();
        List<Integer> list = listStr.stream().map(Integer::parseInt).toList();
        return new ArrayList<Integer>(list);
    }
}
