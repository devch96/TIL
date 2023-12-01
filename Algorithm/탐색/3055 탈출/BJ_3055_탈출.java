import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class BJ_3055_탈출 {
    static int R,C, ans;
    static char [][] map;
    static int [] dx = {-1,0,1,0};
    static int [] dy = {0,1,0,-1};

    static Queue<int[]> point = new LinkedList<int[]>();
    static Queue<int[]> water = new LinkedList<int[]>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine()," ");

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        map = new char[R][C];

        for(int i=0;i<R;i++) {
            String str = br.readLine();
            for(int j=0;j<C;j++) {
                map[i][j]= str.charAt(j);
                if(map[i][j]=='S') {
                    point.offer(new int[] {i,j});
                }
                else if(map[i][j]=='*') {
                    water.offer(new int[] {i,j});
                }
            }
        }

        System.out.println(bfs()? ans:"KAKTUS");

    }

    public static boolean bfs() {

        while(!point.isEmpty()) {
            ans++;
            int size = water.size();
            for(int i=0;i<size;i++) {
                int [] info = water.poll();

                for(int d=0;d<4;d++) {
                    int nx = info[0]+dx[d];
                    int ny = info[1]+dy[d];

                    if(range(nx,ny) && (map[nx][ny]=='.' || (map[nx][ny]=='S'))) {
                        map[nx][ny]='*';
                        water.offer(new int[] {nx,ny});
                    }
                }
            }

            size = point.size();

            for(int i=0;i<size;i++) {
                int [] info = point.poll();

                for(int d=0;d<4;d++) {
                    int nx = info[0]+dx[d];
                    int ny = info[1]+dy[d];

                    if(range(nx,ny)) {

                        if(map[nx][ny]=='.') {
                            map[nx][ny]='S';
                            point.offer(new int[] {nx,ny});
                        }
                        else if(map[nx][ny]=='D') return true;

                    }
                }
            }
        }
        return false;
    }

    public static boolean range(int x, int y) {
        return x>=0 && y>=0 && x<R && y<C;
    }
}
