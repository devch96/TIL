import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_14889_스타트와_링크 {
    static int n;
    static int[][] score;
    static boolean[] visited;
    static int minValue;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        score = new int[n][n];
        visited = new boolean[n];
        minValue = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                score[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        backtrack(0,0);

        System.out.println(minValue);
    }

    private static void backtrack(int index, int depth){
        if(depth == n/2){
            diffScore();
            return;
        }
        for (int i = index; i < n; i++) {
            if (!visited[i]) {
                visited[i] = true;
                backtrack(i + 1,depth+1);
                visited[i] = false;
            }
        }
    }

    private static void diffScore(){
        int startTeamScore = 0;
        int linkTeamScore = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                if (visited[i] && visited[j]) {
                    startTeamScore += (score[i][j] + score[j][i]);
                } else if (!visited[i] && !visited[j]) {
                    linkTeamScore += (score[i][j] + score[j][i]);
                }
            }
        }
        int val = Math.abs(startTeamScore - linkTeamScore);
        if (val == 0) {
            System.out.println(val);
            System.exit(0);
        }
        minValue = Math.min(minValue, val);
    }
}
