import java.io.*;

public class BJ_10989_수_정렬하기_3 {
    public static int[] arr;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }
        br.close();
        radixSort(arr,5);
        for (int i = 0; i < n; i++) {
            bw.write(arr[i] + "\n");
        }
        bw.flush();
        bw.close();
    }

    private static void radixSort(int[] arr, int maxSize) {
        int[] output = new int[arr.length];
        int jarisu = 1;
        int count = 0;
        while (count != maxSize) {
            int[] bucket = new int[10];
            for (int i = 0; i < arr.length; i++) {
                bucket[(arr[i] / jarisu) % 10]++;
            }
            for (int i = 1; i < 10; i++) {
                bucket[i] += bucket[i-1];
            }
            for (int i = arr.length - 1; i >= 0; i--) {
                output[bucket[(arr[i] / jarisu % 10)] -1] = arr[i];
                bucket[(arr[i] / jarisu % 10)]--;
            }
            for (int i = 0; i < arr.length; i++) {
                arr[i] = output[i];
            }
            jarisu *= 10;
            count++;
        }
    }
}
