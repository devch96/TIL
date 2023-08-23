import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1149_RGB거리 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int[][] house = new int[n][3];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            house[i][0] = Integer.parseInt(st.nextToken());
            house[i][1] = Integer.parseInt(st.nextToken());
            house[i][2] = Integer.parseInt(st.nextToken());
        }

        for (int i = 1; i < n; i++) {
            house[i][0] += Math.min(house[i - 1][1], house[i - 1][2]);
            house[i][1] += Math.min(house[i - 1][0], house[i - 1][2]);
            house[i][2] += Math.min(house[i - 1][1], house[i - 1][0]);
        }

        System.out.println(Math.min(Math.min(house[n-1][1],house[n-1][2]),house[n-1][0]));
    }
}
