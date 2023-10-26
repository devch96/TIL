import java.io.*;
import java.util.StringTokenizer;

public class BJ_10972_다음_순열 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        StringBuilder sb = new StringBuilder();
        if(nextPermutation(arr)){
            for (int num : arr){
                sb.append(num).append(" ");
            }
        }else{
            sb.append("-1");
        }
        bw.write(sb.toString());
        bw.flush();
    }

    private static boolean nextPermutation(int arr[]){
        int i = arr.length - 1;

        while (i > 0 && arr[i - 1] >= arr[i]) {
            i--;
        }

        if(i<=0){
            return false;
        }

        int j = arr.length-1;

        while(arr[i-1] >= arr[j]){
            j--;
        }

        swap(j,i-1,arr);

        j = arr.length -1;

        while(i < j) {
            swap(i, j, arr);
            i += 1;
            j -= 1;
        }
        return true;
    }

    private static void swap(int i, int j, int[] arr){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
