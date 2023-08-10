import java.io.*;
import java.util.StringTokenizer;

public class BJ_1052_물병 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int count = n;
        while(true){
            int result = buyBottleCount(count);
            if(result <= k){
                break;
            }else{
                count++;
            }
        }
        bw.write(String.valueOf(count - n));
        bw.flush();

    }
    private static int buyBottleCount(int n){
        int count = 0;
        while(n>0){
            if(n%2 == 1){
                count++;
            }
            n/=2;
        }
        return count;
    }
}