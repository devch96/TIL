import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class BJ_16936_나3곱2 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        long[][] arr = new long[n][2];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            long num = Long.parseLong(st.nextToken());
            arr[i][1] = num;
            while(num % 3 == 0){
                arr[i][0] ++;
                num /= 3;
            }
        }

        Arrays.sort(arr, new Comparator<long[]>() {
            @Override
            public int compare(long[] o1, long[] o2) {
                if (o1[0] == o2[0]) {
                    return Long.compare(o1[1], o2[1]);
                }else{
                    return Long.compare(o2[0],o1[0]);
                }
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(arr[i][1]).append(" ");
        }
        bw.write(sb.toString());
        bw.flush();
    }
}
