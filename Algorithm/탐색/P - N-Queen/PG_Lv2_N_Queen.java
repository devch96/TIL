public class PG_Lv2_N_Queen {
    static int answer;
    static int[] arr;
    static int x;
    public static int solution(int n){
        arr = new int[n];
        x = n;
        dfs(0);
        return answer;
    }

    private static void dfs(int depth){
        if(depth == x){
            answer++;
            return;
        }
        for (int i = 0; i < x; i++) {
            arr[depth] = i;
            if (isAvailable(depth)) {
                dfs(depth + 1);
            }
        }
    }

    private static boolean isAvailable(int col) {
        for (int i = 0; i < col; i++) {
            if (arr[col] == arr[i]) {
                return false;
            } else if (Math.abs(col - i) == Math.abs(arr[col] - arr[i])) {
                return false;
            }
        }
        return true;
    }
}
