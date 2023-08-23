import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_4779_칸토어_집합 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine()) != null){
            int n = Integer.parseInt(str);
            String s = "-".repeat((int) Math.pow(3, n));
            System.out.println(devideAndConquer(s));
        }
    }

    private static String devideAndConquer(String s){
        if(s.length() == 1){
            return s;
        }
        int divergingPoint = s.length()/3;
        String s1 = s.substring(0, divergingPoint);
        String s2 = " ".repeat(divergingPoint);
        String s3 = s.substring(divergingPoint*2);
        return devideAndConquer(s1)+devideAndConquer(s2)+devideAndConquer(s3);
    }
}
