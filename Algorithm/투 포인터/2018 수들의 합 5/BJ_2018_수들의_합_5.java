import java.io.*;

public class BJ_2018_수들의_합_5 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        int count = 1;
        int start_idx = 1;
        int end_idx = 1;
        int sum = 1;
        while (end_idx != n) {
            if(sum == n){
                count ++;
                end_idx++;
                sum += end_idx;
            }else if(sum > n){
                sum -= start_idx;
                start_idx++;
            }else{
                end_idx++;
                sum += end_idx;
            }
        }
        bw.write(String.valueOf(count));
        bw.flush();
    }

}
