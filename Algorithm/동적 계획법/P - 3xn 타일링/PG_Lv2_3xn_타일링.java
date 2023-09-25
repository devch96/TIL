public class PG_Lv2_3xn_타일링 {
    public static int solution(int n){
        long mod = 1_000_000_007;

        if (n % 2 == 1){
            return 0;
        }

        long[] dp = new long[n + 1];
        dp[2] = 3;
        dp[4] = 11;

        for(int i = 6; i <= n; i+=2){
            dp[i] = ((((dp[i - 2] * 4) % mod) - (dp[i - 4] % mod)) + mod) % mod;
        }
        return (int)dp[n];
    }

    public static void main(String[] args) {
        System.out.println(solution(6));
    }
}
