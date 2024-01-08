import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2805_나무_자르기 {
    static int n;
    static int m;
    static int[] arr;
    static int answer;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        arr = new int[n];
        int max = 0;
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            if(arr[i] > max){
                max = arr[i];
            }
        }
        binarySearch(0, max);
        System.out.println(answer);
    }

    private static void binarySearch(int start, int end){
        while(start<=end){
            long result = 0;
            int mid = end + ((start - end) / 2);
            for(int tree : arr){
                if(tree > mid){
                    result += (tree-mid);
                }
            }
            if(result >= m){
                start = mid + 1;
                if(mid >= answer){
                    answer = mid;
                }
            }else{
                end = mid - 1;
            }
        }
    }

}
