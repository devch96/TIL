import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BJ_11444_피보나치_수_6 {
    static long mod = 1_000_000_007;
    static Map<Long, Long> fibMap;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());
        fibMap = new HashMap<>();
        fibMap.put((long) 0, (long) 0);
        fibMap.put((long) 1, (long) 1);
        fibMap.put((long) 2, (long) 1);
        System.out.println(fib(n));
    }

    private static long fib(long n) {
        if (fibMap.containsKey(n)) {
            return fibMap.get(n);
        }
        long temp = 0;
        if(n%2 == 0){
            temp = ((fib(n/2) * fib(n/2) % mod) + (fib(n/2)*  2 * fib(n/2 -1) % mod)) %mod;
        }else{
            temp = ((fib(n/2) * fib(n/2) % mod) + (fib(n/2+1) * fib(n/2+1) % mod)) % mod;
        }
        fibMap.put(n,temp);
        return temp;
    }
}
