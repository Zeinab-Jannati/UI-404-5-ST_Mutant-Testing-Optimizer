public class LogicTest {

    public boolean checkStatus(int a, int b) {

        if (a > 10 && b < 5) {
            return false;
        }

        int c = a | b;

        int d = c << 1;

        return d > 100;
    }
}
