import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2022_사다리 {
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        double x = Double.parseDouble(st.nextToken());
        double y = Double.parseDouble(st.nextToken());
        double c = Double.parseDouble(st.nextToken());

        double start = 0;
        double end = Math.min(x,y);
        while (end - start >= 0.001) {
            double mid = (start + end) / 2;
            double h1 = Math.sqrt(Math.pow(x, 2) - Math.pow(mid, 2));
            double h2 = Math.sqrt(Math.pow(y, 2) - Math.pow(mid, 2));
            double result = (h1 * h2) / (h1 + h2);
            if (result >= c) {
                start = mid;
            }else{
                end = mid;
            }
        }
        System.out.printf("%.3f%n",end);
    }
}
