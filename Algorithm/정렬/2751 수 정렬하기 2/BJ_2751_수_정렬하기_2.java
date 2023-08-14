import java.io.*;

public class BJ_2751_수_정렬하기_2 {
    public static int[] A, temp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        A = new int[n+1];
        temp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            A[i] = Integer.parseInt(br.readLine());
        }
        mergerSort(1,n);
        for (int i = 1; i <= n; i++) {
            bw.write(A[i] + "\n");
        }
        bw.flush();
        bw.close();
    }

    private static void mergerSort(int start, int end) {
        if(end - start < 1){
            return;
        }
        int middle = start + (end - start)/2; // Integer 범위 생각
        mergerSort(start,middle);
        mergerSort(middle+1,end);
        for (int i = start; i <= end; i++) {
            temp[i] = A[i];
        }
        int k = start;
        int index1 = start;
        int index2 = middle+1;
        while(index1 <= middle && index2 <= end){
            if(temp[index1] > temp[index2]){
                A[k] = temp[index2];
                k++;
                index2++;
            }else{
                A[k] = temp[index1];
                k++;
                index1++;
            }
        }
        while(index1 <= middle){
            A[k] = temp[index1];
            k++;
            index1++;
        }
        while (index2 <= end) {
            A[k] = temp[index2];
            k++;
            index2++;
        }
    }
}
