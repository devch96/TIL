import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1963_소수_경로 {
    static class Node{
        int number;
        int count;

        public Node(int number, int count) {
            this.number = number;
            this.count = count;
        }
    }

    static boolean[] prime;
    static boolean[] visited;
    static int start;
    static int target;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        prime = new boolean[10001];
        primeCheck();
        for (int i = 0; i < t; i++) {
            visited = new boolean[10001];
            StringTokenizer st = new StringTokenizer(br.readLine());
            start = Integer.parseInt(st.nextToken());
            target = Integer.parseInt(st.nextToken());
            int answer = bfs();
            System.out.println(answer == -1 ? "Impossible" : answer);
        }
    }

    private static void primeCheck(){
        Arrays.fill(prime, true);
        for (int i = 2; i * i <= 10000 ; i++) {
            if(prime[i]){
                for (int j = i*i; j <= 10000 ; j+=i) {
                    prime[j] = false;
                }
            }
        }
    }
    private static int bfs(){
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(x -> x.count));
        queue.offer(new Node(start, 0));
        visited[start] = true;
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            int num = now.number;
            int count = now.count;
            if(num == target){
                return count;
            }
            for (int i = 0; i < 4; i++) {
                int temp = cutDigit(num,i);
                for (int j = 0; j <= 9; j++) {
                    int nextNum = (int) (temp + (j*Math.pow(10,i)));
                    if(nextNum < 1000 || !prime[nextNum] || num == nextNum){
                        continue;
                    }
                    if (!visited[nextNum]) {
                        visited[nextNum] = true;
                        queue.offer(new Node(nextNum, count + 1));
                    }
                }
            }
        }
        return -1;
    }

    private static int cutDigit(int num, int digit){
        switch (digit){
            case 0:
                return (num / 10) * 10;
            case 1:
                int underOneDigit = num % 10;
                return ((num / 100) * 100) + underOneDigit;
            case 2:
                int underTenDigit = num % 100;
                return ((num / 1000) * 1000) + underTenDigit;
            case 3:
                return num % 1000;
        }
        return -1;
    }

}
