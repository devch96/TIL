import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_9019_DSLR {
    static char[] q = {'D', 'S', 'L', 'R'};

    static boolean[] visited;

    static StringBuilder[] commands;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        for (int x = 0; x < t; x++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int target = Integer.parseInt(st.nextToken());
            visited = new boolean[10000];
            commands = new StringBuilder[10000];
            for (int i = 0; i < 10000; i++) {
                commands[i] = new StringBuilder();
            }
            bfs(start, target);
        }
    }

    private static void bfs(int start, int target){
        Set<Integer> visited = new HashSet<>();
        Queue<String> commands = new ArrayDeque<>();
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);
        commands.add("");
        while (!queue.isEmpty()) {
            int now = queue.poll();
            String cmd = commands.poll();
            if(now == target){
                System.out.println(cmd);
                return;
            }
            for (int i = 0; i < 4; i++) {
                char c = q[i];
                int next = command(now, c);
                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                    commands.add(cmd + c);
                }
            }
        }
    }

    private static int command(int num, char c){
        switch (c){
            case 'D' : {
                return num*2 % 10000;
            }
            case 'S' :{
                num -= 1;
                return num < 0 ? 9999 : num;
            }
            case 'L': {
                return (num % 1000) * 10 + num / 1000;
            }
            case 'R': {
                return (num % 10) * 1000 + num / 10;
            }
        }
        return -1;
    }
}
