import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_17406_배열_돌리기_4 {
    static int n;
    static int m;
    static int K;
    static int[][] map;
    static int[][] cycle;
    static int min = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[n][m];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        cycle = new int[K][3];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            cycle[i][0] = Integer.parseInt(st.nextToken()) - 1;
            cycle[i][1] = Integer.parseInt(st.nextToken()) - 1;
            cycle[i][2] = Integer.parseInt(st.nextToken());
        }
        
        permutation(0, new int[K], new boolean[K]);

        System.out.println(min);
    }
    
    private static void permutation(int cnt, int[] arr , boolean[] visited) {
        if(cnt == K) {
            doCycle(arr);
            return;
        }
        for (int i = 0; i < K; i++) {
            if (visited[i]) {
                continue;
            }
            visited[i] = true;
            arr[cnt] = i;
            permutation(cnt+1, arr, visited);
            visited[i] = false;
        }
    }

    public static void doCycle(int[] arr) {
        int[][] tmp = copyMap();

        for(int k=0; k<K; k++) {
            int r = cycle[arr[k]][0];
            int c = cycle[arr[k]][1];
            int S = cycle[arr[k]][2];

            for(int s=1; s<=S; s++) {
                //위
                int upTmp = tmp[r-s][c+s];
                for(int y = c+s; y > c-s; y--) {
                    tmp[r-s][y] = tmp[r-s][y-1];
                }
                //오른쪽
                int rightTmp = tmp[r+s][c+s];
                for(int x = r+s; x > r-s; x--) {
                    tmp[x][c+s] = tmp[x-1][c+s];
                }
                tmp[r-s+1][c+s] = upTmp;
                //아래
                int leftTmp = tmp[r+s][c-s];
                for(int y = c-s; y < c+s; y++) {
                    tmp[r+s][y] = tmp[r+s][y+1];
                }
                tmp[r+s][c+s-1] = rightTmp;
                //왼쪽
                for(int x = r-s; x < r+s; x++) {
                    tmp[x][c-s] = tmp[x+1][c-s];
                }
                tmp[r+s-1][c-s] = leftTmp;
            }
        }

        getAnswer(tmp);
    } 
    
    private static int[][] copyMap() {
        int[][] result = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = map[i][j];
            }
        }
        return result;
    }

    public static void getAnswer(int[][] tmp) {
        for(int i=0; i<n; i++) {
            int sum = 0;
            for(int j=0; j<m; j++) {
                sum += tmp[i][j];
            }
            if(min > sum) min = sum;
        }
    }
}
