import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_13397_구간_나누기_2 {
    static int[] arr;
    static int n;
    static int m;
    static int answer;
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        arr = new int[n];
        st = new StringTokenizer(br.readLine());
        int max = 0;
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            max = Math.max(max, arr[i]);
        }
        answer = max;
        binarySearch(0, max);
        System.out.println(answer);
    }

    private static void binarySearch(int start, int end){
        while (start <= end) {
            int mid = end + (start - end) / 2;
            if (isValid(mid)) {
                answer = Math.min(answer, mid);
                end = mid - 1;
            }else{
                start = mid + 1;
            }
        }

    }

    private static boolean isValid(int num){
        int max = arr[0];
        int min = arr[0];
        int count = 1;
        for (int i = 0; i < n; i++) {
            max = Math.max(arr[i], max);
            min = Math.min(arr[i], min);
            if (max - min > num) {
                count++;
                max = arr[i];
                min = arr[i];
            }
        }
        return count <= m;
    }
}
