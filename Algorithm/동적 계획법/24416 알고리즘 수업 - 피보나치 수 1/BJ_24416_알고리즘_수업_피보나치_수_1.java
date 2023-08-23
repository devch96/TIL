import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_24416_알고리즘_수업_피보나치_수_1 {
    static int result1;
    static int result2;

    static int arr[];
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        result1 = 0;
        result2 = 0;
        arr = new int[n + 1];
        arr[0]=0;
        arr[1]=1;
        fib(n);
        fibonacci(n);
        System.out.println(result1 + " " + result2);
    }

    private static int fib(int n){
        if(n==1 || n==2){
            result1++;
            return 1;
        }
        return(fib(n-1) + fib(n-2));
    }

    private static int fibonacci(int n){
        for (int i = 3; i <= n; i++) {
            result2++;
            arr[i] = arr[i-1] + arr[i-2];
        }
        return arr[n];
    }
}
