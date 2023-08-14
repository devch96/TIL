import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2343_기타_레슨 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int[] arr = new int[n];
        int start = 0;
        int end = 0;
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            if(start < arr[i]){
                start = arr[i];
            }
            end += arr[i];
        }
        System.out.println(binarySearch(arr,start,end,n,m));
    }

    private static int binarySearch(int[] arr, int start, int end, int n, int m){
        while(start <= end){
            int middle = start + (end - start)/2;
            int sum = 0;
            int count = 0;
            for (int i = 0; i < n; i++) {
                if (sum + arr[i] > middle) {
                    count++;
                    sum = 0;
                }
                sum = sum + arr[i];
            }
            if (sum != 0) {
                count++;
            }
            if(count > m){
                start = middle+1;
            }else{
                end = middle-1;
            }
        }
        return start;
    }
}
