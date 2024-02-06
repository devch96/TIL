import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_17089_세_친구 {
    private static boolean[][] isFriend;
    private static int[] friendCnt;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        isFriend = new boolean[n + 1][n + 1];
        friendCnt = new int[n + 1];

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int first = Integer.parseInt(st.nextToken());
            int second = Integer.parseInt(st.nextToken());
            isFriend[first][second] = true;
            isFriend[second][first] = true;
            friendCnt[first]++;
            friendCnt[second]++;
        }

        int answer = Integer.MAX_VALUE;

        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (!isFriend[i][j]) {
                    continue;
                }
                for (int k = j + 1; k <= n; k++) {
                    if (!isFriend[i][k] || !isFriend[j][k]) {
                        continue;
                    }
                    int sum = friendCnt[i] + friendCnt[j] + friendCnt[k] - 6;
                    answer = Math.min(answer, sum);
                }
            }
        }
        System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
    }
}
