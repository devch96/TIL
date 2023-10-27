import java.io.*;
import java.util.StringTokenizer;

public class BJ_10819_차이를_최대로 {
    static int[] arr;
    static int[] sequence;
    static int maxValue;
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        arr = new int[n];
        sequence = new int[n];
        visited = new boolean[n];
        maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            sequence[i] = Integer.parseInt(st.nextToken());
        }
        backtrack(0);
        bw.write(String.valueOf(maxValue));
        bw.flush();
    }

    private static void backtrack(int depth) {
        if(depth == sequence.length){
            maxValue = Math.max(maxValue, sum());
            return;
        }

        for (int i = 0; i < sequence.length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                arr[depth] = sequence[i];
                backtrack(depth+1);
                visited[i] = false;
            }
        }
    }

    private static int sum(){
        int result = 0;
        for (int i = 0; i < arr.length-1; i++) {
            result += Math.abs(arr[i] - arr[i+1]);
        }
        return result;
    }
}
