
import java.util.Scanner;

public class test {

    public static void main(String[] args) {

        String Systemin = "kram";

        Scanner scan = new Scanner(Systemin);
        String input = scan.next();
        int antalB = 0;
        int antalK = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 'b') {
                antalB++;
            } else if (c == 'k') {
                antalK++;
            }
        }
        if (antalB == 0 && antalK == 0) {
            System.out.println("none");
        } else if (antalB > antalK) {
            println("boba");
        } else if (antalK > antalB) {
            println("kiki");
        } else {
            println("boki");
        }
        scan.close();

    }

    static void println(Object... inp) {
        for (int i = 0; i < inp.length; i++) {
            Object object = inp[i];
            System.out.print(object);
        }
        System.out.println();
    }

    public static int intConv(String tal) {
        return Integer.parseInt(tal);
    }

    public static int intConv(double tal) {
        return (int) tal;
    }

}
