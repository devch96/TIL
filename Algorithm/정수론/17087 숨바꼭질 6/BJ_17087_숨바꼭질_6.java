import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_17087_숨바꼭질_6 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int s = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int[] distance = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Math.abs(s - Integer.parseInt(st.nextToken()));
        }
        int answer = 0;
        for (int i = 0; i < n; i++) {
            answer = gcd(answer, distance[i]);
        }
        System.out.println(answer);
    }

    private static int gcd(int x, int y){
        if(y == 0){
            return x;
        }else{
            return gcd(y, x%y);
        }
    }
}
