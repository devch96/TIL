import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PG_Lv2_교점에_별_만들기 {
    static List<int[]> point;
    static long startX = Long.MAX_VALUE;
    static long endX = Long.MIN_VALUE;
    static long startY = Long.MAX_VALUE;
    static long endY = Long.MIN_VALUE;
    public static String[] solution(int[][] line){
        point = new ArrayList<>();
        getIntegerIntersection(line);
        long c = endX - startX + 1;
        long r = endY - startY + 1;

        String[][] graph = new String[(int) r][(int) c];
        for(String[] s : graph){
            Arrays.fill(s, ".");
        }
        for(int[] intersection : point){
            long x = intersection[0] - startX;
            long y = endY - intersection[1];
            graph[(int) y][(int) x] = "*";
        }
        String[] answer = new String[graph.length];
        for (int i = 0; i < graph.length; i++) {
            answer[i] = String.join("", graph[i]);
        }
        return answer;
    }

    private static void getIntegerIntersection(int[][] line){
        for (int i = 0; i < line.length; i++) {
            long A = line[i][0];
            long B = line[i][1];
            long E = line[i][2];
            for (int j = i + 1; j < line.length; j++) {
                long C = line[j][0];
                long D = line[j][1];
                long F = line[j][2];

                long up_x = (B * F) - (E * D);
                long up_y = (E * C) - (A * F);
                long down = (A * D) - (B * C);
                if(down ==0) continue;
                long x= up_x/ down;
                long y= up_y/ down;
                if(up_x % down == 0 && up_y % down == 0){
                    startX = Math.min(startX, x);
                    endX = Math.max(endX, x);
                    startY = Math.min(startY, y);
                    endY = Math.max(endY,y);
                    point.add(new int[]{(int)x, (int)y});
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new int[][] {{2,-1,4},{-2,-1,4},{0,-1,1},{5,-8,-12},{5,8,12}})));
    }
}
