import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1916_최소비용_구하기 {
    static class Bus implements Comparable<Bus>{
        int destination;
        int fee;
        Bus(int destination, int fee){
            this.destination = destination;
            this.fee = fee;
        }

        @Override
        public int compareTo(Bus o) {
            if (this.fee > o.fee) {
                return 1;
            }
            else{
                return -1;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());

        List<Bus>[] arrList = new List[n + 1];
        boolean[] visited = new boolean[n + 1];
        int[] money = new int[n + 1];
        Arrays.fill(money, Integer.MAX_VALUE);

        for (int i = 1; i <= n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());
            int fee = Integer.parseInt(st.nextToken());
            arrList[start].add(new Bus(destination, fee));
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        int startCity = Integer.parseInt(st.nextToken());
        int destinationCity = Integer.parseInt(st.nextToken());
        money[startCity] = 0;
        Queue<Bus> queue = new PriorityQueue<>();
        queue.add(new Bus(startCity, 0));
        while (!queue.isEmpty()) {
            Bus nowBus = queue.poll();
            int now = nowBus.destination;
            if (!visited[now]) {
                visited[now] = true;
                for (Bus b : arrList[now]) {
                    if (!visited[b.destination] && money[b.destination] > money[now] + b.fee) {
                        money[b.destination] = money[now] + b.fee;
                        queue.add(new Bus(b.destination, money[b.destination]));
                    }
                }
            }
        }
        System.out.println(money[destinationCity]);
    }
}
