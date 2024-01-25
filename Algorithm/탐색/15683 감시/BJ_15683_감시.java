import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_15683_감시 {
    static class CCTV {
        int x;
        int y;
        int type;

        public CCTV(int x, int y, int type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
    static int n;
    static int m;
    static int answer;
    static List<CCTV> cctvList;
    static int[][] dist = {{}, {0, 1, 2, 3}, {}, {}, {}, {}};

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        answer = Integer.MAX_VALUE;
        int[][] office = new int[n][m];
        cctvList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                office[i][j] = Integer.parseInt(st.nextToken());
                if (office[i][j] != 6 && office[i][j] != 0) {
                    cctvList.add(new CCTV(i, j, office[i][j]));
                }
            }
        }
        dfs(0, office);
        System.out.println(answer);
    }

    private static void dfs(int depth, int[][] office) {
        if (depth == cctvList.size()) {
            answer = Math.min(answer, getBlindSpot(office));
            return;
        }
        CCTV curCCTV = cctvList.get(depth);
        int x = curCCTV.x;
        int y = curCCTV.y;
        int cctvType = curCCTV.type;
        int[][] temp;
        if(cctvType == 1) {
            temp = copyMap(office);
            checkLeft(temp, x, y);
            dfs(depth + 1, temp);

            temp = copyMap(office);
            checkRight(temp, x, y);
            dfs(depth + 1, temp);

            temp = copyMap(office);
            checkDown(temp, x, y);
            dfs(depth + 1, temp);

            temp = copyMap(office);
            checkUp(temp, x, y);
            dfs(depth + 1, temp);
        } else if (cctvType==2) {
            temp = copyMap(office);
            checkLeft(temp, x, y);
            checkRight(temp, x, y);
            dfs(depth+1, temp);

            temp = copyMap(office);
            checkUp(temp, x, y);
            checkDown(temp, x, y);
            dfs(depth+1, temp);
        } else if (cctvType==3) {
            temp = copyMap(office);
            checkLeft(temp, x, y);
            checkUp(temp, x, y);
            dfs(depth+1, temp);

            temp = copyMap(office);
            checkUp(temp, x, y);
            checkRight(temp, x, y);
            dfs(depth+1, temp);

            temp = copyMap(office);
            checkRight(temp, x, y);
            checkDown(temp, x, y);
            dfs(depth+1, temp);

            temp = copyMap(office);
            checkDown(temp, x, y);
            checkLeft(temp, x, y);
            dfs(depth+1, temp);

        } else if (cctvType == 4) {
            temp = copyMap(office);
            checkLeft(temp, x, y);
            checkUp(temp, x, y);
            checkRight(temp, x, y);
            dfs(depth + 1, temp);

            temp = copyMap(office);
            checkUp(temp, x, y);
            checkRight(temp, x, y);
            checkDown(temp, x, y);
            dfs(depth + 1, temp);

            temp = copyMap(office);
            checkRight(temp, x, y);
            checkDown(temp, x, y);
            checkLeft(temp, x, y);
            dfs(depth + 1, temp);

            temp = copyMap(office);
            checkDown(temp, x, y);
            checkLeft(temp, x, y);
            checkUp(temp, x, y);
            dfs(depth + 1, temp);
        } else {
            temp = copyMap(office);
            checkRight(temp, x, y);
            checkDown(temp, x, y);
            checkLeft(temp ,x , y);
            checkUp(temp, x, y);
            dfs(depth+1, temp);
        }
    }

    private static int getBlindSpot(int[][] office) {
        int blindSpot = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (office[i][j] == 0) {
                    blindSpot++;
                }
            }
        }
        return blindSpot;
    }

    private static void checkLeft(int[][] office, int x, int y) {
        for(int i=y-1; i>=0; i--) {
            if(office[x][i] == 6) return;
            if(office[x][i] != 0) continue;
            office[x][i] = -1;
        }
    }

    private static void checkRight(int[][] office, int x, int y) {
        for(int i=y+1; i<m; i++) {
            if(office[x][i] == 6) return;
            if(office[x][i] != 0) continue;
            office[x][i] = -1;
        }
    }

    private static void checkUp(int[][] office, int x, int y) {
        for(int i=x-1; i>=0; i--) {
            if(office[i][y] == 6) return;
            if(office[i][y] != 0) continue;
            office[i][y] = -1;
        }
    }

    private static void checkDown(int[][] office, int x, int y) {
        for(int i=x+1; i<n; i++) {
            if(office[i][y] == 6) return;
            if(office[i][y] != 0) continue;
            office[i][y] = -1;
        }
    }

    private static int[][] copyMap(int[][] map) {
        int[][]tmp = new int[n][m];
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                tmp[i][j] = map[i][j];
            }
        }
        return tmp;
    }
}
