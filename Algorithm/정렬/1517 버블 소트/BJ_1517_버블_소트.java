import java.io.*;
import java.util.StringTokenizer;

public class BJ_1517_버블_소트 {
    public static int[] arr, temp;
    public static long result;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        arr = new int[n+1];
        temp = new int[n+1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        result = 0;
        mergeSort(1,n);
        System.out.println(result);
    }

    private static void mergeSort(int start, int end) {
        if(end - start < 1){
            return;
        }
        int middle = start + (end - start)/2;
        mergeSort(start,middle);
        mergeSort(middle+1, end);
        for (int i = start; i <= end; i++) {
            temp[i] = arr[i];
        }
        int k = start;
        int left = start;
        int right = middle+1;
        while (left <= middle && right <= end) {
            if (temp[left] > temp[right]) {
                arr[k] = temp[right];
                result += right - k;
                right++;
            } else{
                arr[k] = temp[left];
                left++;
            }
            k++;
        }
        while (left <= middle) {
            arr[k] = temp[left];
            k++;
            left++;
        }
        while(right <= end){
            arr[k] = temp[right];
            k++;
            right++;
        }
    }
}
