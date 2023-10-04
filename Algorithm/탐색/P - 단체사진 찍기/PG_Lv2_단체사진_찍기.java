import java.util.ArrayList;
import java.util.List;

public class PG_Lv2_단체사진_찍기 {
    static char[] arr = {'A','C','F','J','M','N','R','T'};
    static boolean[] visited;
    static List<Point> pointList;
    static StringBuilder sb;
    static int answer;


    public static int solution(int n, String[] data){
        answer = 0;
        visited = new boolean[8];
        pointList = new ArrayList<>();
        sb = new StringBuilder();
        for(String d : data){
            pointList.add(new Point(d.charAt(0), d.charAt(2), d.charAt(3), d.charAt(4) - '0'));
        }
        dfs(sb,0);
        return answer;
    }

    private static void dfs(StringBuilder now, int depth){
        if(depth>=8){
            if (check(now.toString())) {
                answer++;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (!visited[i]) {
                visited[i] = true;
                now.append(arr[i]);
                dfs(now,depth+1);
                now.deleteCharAt(now.length() - 1);
                visited[i] = false;
            }
        }
    }

    private static boolean check(String now){
        for (Point p : pointList) {
            int index1 = now.indexOf(p.from);
            int index2 = now.indexOf(p.to);

            switch (p.condition) {
                case '=':
                    if (!(Math.abs(index1 - index2) == p.num + 1)) {
                        return false;
                    }
                    break;
                case '<':
                    if (!(Math.abs(index1 - index2) < p.num + 1)) {
                        return false;
                    }
                    break;
                case '>':
                    if (!(Math.abs(index1 - index2) > p.num + 1)) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    static class Point{
        char from;
        char to;
        char condition;
        int num;

        public Point(char from, char to, char condition, int num) {
            this.from = from;
            this.to = to;
            this.condition = condition;
            this.num = num;
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(2, new String[] {"N~F=0", "R~T>2"}));
    }
}
