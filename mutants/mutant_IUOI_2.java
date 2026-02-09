public class MathTest {
    public int calculate(int a, int b) {

        int res = a + (++b);


        if (res == 100) {
            return 0;
        }


        return -res;
    }
}
