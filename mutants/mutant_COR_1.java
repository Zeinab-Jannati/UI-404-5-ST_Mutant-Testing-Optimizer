public class IntegrationTest {

    public int processData(int val1, int val2) {
        if (val1 > 0 || val2 > 0) {
            return helper(val1, val2);
        }
        return helper(val2, val1);
    }

    public int helper(int a, int b) {
        int result = a * 2;
        result = result - b;
        return result;
    }
}
