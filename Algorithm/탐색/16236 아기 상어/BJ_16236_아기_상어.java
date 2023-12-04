import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_16236_아기_상어 {
    static int N;
    static int[][] map; // 입력 받은 원본 배열
    static int[][] dist; // 여러 물고기가 있을 때 거리 체크할 배열
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, 1, 0, -1};
    static int size = 2; // 상어 사이즈
    static int eat = 0; // 상어가 물고기 먹은 횟수
    static int cnt = 0; // 상어가 이동한 횟수
    static int sharkX = -1; // 상어의 X 좌표
    static int sharkY = -1; // 상어의 Y 좌표
    static int minX, minY, minDist; // while문 탈출 조건으로 쓸 변수

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());

        map = new int[N][N];
        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            for(int j=0; j<N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if(map[i][j] == 9) {
                    sharkX = i;
                    sharkY = j;
                    map[i][j] = 0; // 상어가 있었던 위치도 이 후로 탐색해야 하므로 0으로 초기화
                }
            }
        }

        while(true) {
            dist = new int[N][N]; // 거리 알아낼 배열 && 자동으로 방문 처리 됨
            minX = Integer.MAX_VALUE; // 상어에서 가장 가까이 있는 물고기 X좌표
            minY = Integer.MAX_VALUE; // 상어에서 가장 가까이 있는 물고기 Y좌표
            minDist = Integer.MAX_VALUE; // 상어에서 가장 가까이 있는 물고기 거리

            bfs(sharkX, sharkY); // 가장 가까이 있는 먹을 수 있는 물고기 먹으러 가는 메소드

            // 먹을 수 있는 물고기의 위치로 이동했다면
            if(minX != Integer.MAX_VALUE && minY != Integer.MAX_VALUE) {
                eat++; // 상어가 먹은 물고기 횟수
                map[minX][minY] = 0; // 물고기를 먹었으므로 해당 위치는 0으로 갱신
                sharkX = minX; // 먹은 물고기의 X좌표가 현재 상어의 X좌표
                sharkY = minY; // 먹은 물고기의 Y좌표가 현재 상어의 Y좌표
                cnt += dist[minX][minY]; // 상어가 이동한 횟수는 해당 dist배열의 값을 더한 값이다

                // 물고기를 먹은 횟수가 상어의 현재 사이즈와 같다면
                if(eat == size) {
                    size++; // 상어의 사이즈 + 1
                    eat = 0; // 물고기 먹은 횟수 0으로 갱신
                }
            } else { // minX, minY가 초기값과 같다면 더 이상 먹을 물고기가 없다는 뜻이다.
                break;
            }
        }

        System.out.println(cnt);
    }

    static void bfs(int x, int y) {
        Queue<Pos> que = new LinkedList<>(); // 메소드가 호출 될 때 마다 새로운 큐를 사용해야 하므로 여기에 큐 생성
        que.add(new Pos(x, y));

        while(!que.isEmpty()) {

            Pos p = que.poll();

            int curX = p.x;
            int curY = p.y;

            for(int t=0; t<4; t++) {
                int nx = curX + dx[t];
                int ny = curY + dy[t];
                // 새롭게 탐색한 위치가 범위 안 && 그 위치가 탐색할 수 있는 위치 && 아직 방문한 적 없는 위치 라면?
                if(isArea(nx, ny) && isAblePos(nx, ny) && dist[nx][ny] == 0) {
                    // 거리 배열에 (이전 배열의 값 + 1)의 값을 넣어주어 최소 거리를 입력함
                    dist[nx][ny] = dist[curX][curY] + 1;

                    // 해당 위치에 물고기가 있고 그 물고기가 상어보다 크기가 작다면?
                    if(isEat(nx, ny)) {
                        // 탐색한 위치에 있는 물고기의 거리가 가장 가까운 지 알아봄
                        if(minDist > dist[nx][ny]) { // 더 가까운 물고기라면?
                            minDist = dist[nx][ny]; // 최소 거리 갱신
                            minX = nx; // 해당 X좌표 갱신
                            minY = ny; // 해당 Y좌표 갱신
                        } else if(minDist == dist[nx][ny]) { // 최소 거리인 물고기가 많다면?
                            // 더 위에 있는 물고기 선택
                            if(minX == nx) {
                                // 그 위에 있는 물고기의 수가 많다면 가장 왼쪽에 있는 물고기 선택
                                if(minY > ny) {
                                    minX = nx;
                                    minY = ny;
                                }
                            } else if(minX > nx) { // 더 위에 있는 물고기가 한 마리 라면
                                minX = nx;
                                minY = ny;
                            }
                        }
                    }

                    que.add(new Pos(nx, ny));
                }
            }

        }
    }

    // map 범위 안에 있는지?
    static boolean isArea(int x, int y) {
        return x >= 0 && y >= 0 && x < N && y < N;
    }

    // 상어가 먹을 수 있는 물고기인지? -> 상어 사이즈보다 작아야 됨
    static boolean isEat(int x, int y) {
        return map[x][y] != 0 && map[x][y] < size;
    }

    // 상어가 해당 위치로 이동할 수 있는지? -> 해당 위치가 비어 있거나(0) 물고기가 있다면 상어보다 크기가 작아야 됨
    static boolean isAblePos(int x, int y) {
        return map[x][y] <= size;
    }

    static class Pos{
        int x, y;

        Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
