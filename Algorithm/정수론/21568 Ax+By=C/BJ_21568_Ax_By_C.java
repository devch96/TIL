import java.io.*;
import java.util.StringTokenizer;

public class BJ_21568_Ax_By_C {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        long gcd = gcd(a,b);
        if (c % gcd != 0) {
            System.out.println(-1);
        }else{
            int mok = (int) (c / gcd);
            long[] ret = euclidean(a, b);
            System.out.println(ret[0] * mok + " " + ret[1] * mok);
        }
    }

    private static long[] euclidean(long a, long b){
        long[] ret = new long[2];
        if(b==0){
            ret[0] = 1; ret[1] = 0;
            return ret;
        }
        long q = a/b;
        long[] v = euclidean(b, a % b);
        ret[0] = v[1];
        ret[1] = v[0] - v[1] * q;
        return ret;
    }

    private static long gcd(long a, long b){
        if(b == 0){
            return a;
        }else{
            return gcd(b, a % b);
        }
    }
}
