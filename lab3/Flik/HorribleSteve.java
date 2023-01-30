public class HorribleSteve {
    public static void main(String [] args) {
        int j = 0; int i = 0;
        for (; i < 500; ++i, ++j) {
            System.out.println(i + " " + j + "\n");
            if (!Flik.isSameNumber(i, j)) {
                break; // break exits the for loop!
            }
        }
        System.out.println("i is " + i);
    }
}
