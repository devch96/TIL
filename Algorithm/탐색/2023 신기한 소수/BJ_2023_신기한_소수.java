import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_2023_신기한_소수 {
    public static int n;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        dfs(2,1);
        dfs(3,1);
        dfs(5,1);
        dfs(7,1);
    }

    private static void dfs(int num, int digit){
        if(digit == n){
            if(isPrime(num)){
                System.out.println(num);
            }
            return;
        }
        for (int i = 1; i <= 9; i+=2) {
            if(isPrime(num * 10 + i)){
                dfs(num*10 +i, digit+1);
            }
        }
    }

    private static boolean isPrime(int num){
        if(num == 1){
            return false;
        } else if (num == 2) {
            return true;
        }else{
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if(num % i == 0){
                    return false;
                }
            }
        }
        return true;
    }
}
