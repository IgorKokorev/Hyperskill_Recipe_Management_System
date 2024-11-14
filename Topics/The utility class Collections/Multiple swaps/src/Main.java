import java.util.*;

class Main {
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        // put your code here
        ArrayList<Integer> list = getIntegerList();
        
        int numberOfPairs = Integer.parseInt(scanner.nextLine());
        List<List<Integer>> pairs = new ArrayList<>();

        if (list.isEmpty()) {return;}
        
        for (int i = 0; i < numberOfPairs; i++) {
            pairs.add(getIntegerList());
        }

        for (List<Integer> pair : pairs) {
            Integer first = list.get(pair.get(0));
            Collections.swap(list, pair.get(0), pair.get(1));
        }
        
        System.out.print(list.getFirst());
        for (int i = 1; i < list.size(); i++) {
            System.out.print(" " + list.get(i));
        }
        System.out.println();
    }
    
    private static ArrayList<Integer> getIntegerList() {
        List<String> listStr = Arrays.stream(scanner.nextLine().split(" ")).toList();
        List<Integer> list = listStr.stream().map(Integer::parseInt).toList();
        return new ArrayList<Integer>(list);
    }
}