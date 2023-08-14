import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_1747_소수_팰린드롬 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] sieve = new int[1003002];
        for (int i = 2; i < sieve.length; i++) {
            sieve[i] = i;
        }
        for (int i = 2; i < Math.sqrt(sieve.length); i++) {
            if(sieve[i] == 0){
                continue;
            }
            for (int j = i + i; j < sieve.length; j += i) {
                sieve[j] = 0;
            }
        }
        for (int i = n; i < sieve.length; i++) {
            if(sieve[i] != 0 && isPalindrome(sieve[i])){
                System.out.println(sieve[i]);
                break;
            }
        }
    }

    private static boolean isPalindrome(int num){
        String strNum = String.valueOf(num);
        StringBuilder sb = new StringBuilder(strNum).reverse();
        return strNum.equals(sb.toString());
    }
}
