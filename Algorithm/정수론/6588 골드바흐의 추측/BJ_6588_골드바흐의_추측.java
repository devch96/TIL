import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_6588_골드바흐의_추측 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean[] prime = new boolean[1_000_001];
        prime[0] = true;
        prime[1] = true;
        for (int i = 2; i < prime.length; i++) {
            if(!prime[i]){
                for (int j = i + i; j < prime.length; j += i) {
                    prime[j] = true;
                }
            }
        }
        int num = Integer.parseInt(br.readLine());
        while(num != 0){
            boolean isPrime = false;
            for (int i = 2; i <= num / 2; i++) {
                if (!prime[i] && !prime[num - i]) {
                    System.out.println(num + " = " + i + " + " + (num - i));
                    isPrime = true;
                    break;
                }
            }
            if(!isPrime){
                System.out.println("Goldbach's conjecture is wrong.");
            }

            num = Integer.parseInt(br.readLine());
        }
    }
}
