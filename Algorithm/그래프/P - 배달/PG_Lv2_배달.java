import java.util.*;

public class PG_Lv2_배달 {
    static class Node implements Comparable<Node>{
        int destination;
        long value;

        public Node(int destination, long value) {
            this.destination = destination;
            this.value = value;
        }

        @Override
        public int compareTo(Node o) {
            if(this.value > o.value){
                return 1;
            } else if (this.value == o.value) {
                return 0;
            }else{
                return -1;
            }
        }
    }
    public static int solution(int N, int[][] road, int K){
        List<Node>[] arrList = new List[N + 1];
        boolean[] visited = new boolean[N + 1];
        long[] distance = new long[N + 1];
        Arrays.fill(distance,Long.MAX_VALUE);
        for (int i = 1; i <= N; i++) {
            arrList[i] = new ArrayList<>(); 
        }
        for (int[] nodes : road) {
            int start = nodes[0];
            int destination = nodes[1];
            int value = nodes[2];
            arrList[start].add(new Node(destination,value));
            arrList[destination].add(new Node(start,value));
        }

        Queue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(1,0));
        distance[1] = 0;
        while (!queue.isEmpty()) {
            Node now = queue.poll();
            int nextVertex = now.destination;
            if (!visited[nextVertex]) {
                visited[nextVertex] = true;
                for (Node temp : arrList[nextVertex]) {
                    int next = temp.destination;
                    long value = temp.value;
                    if (distance[next] > distance[nextVertex] + value) {
                        distance[next] = distance[nextVertex] + value;
                        queue.add(new Node(next, distance[next]));
                    }
                }
            }
        }
        int answer = 0;
        for(long i : distance){
            if( i<=K){
                answer++;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(solution(5,new int[][] {{1,2,1},{2,3,3},{5,2,2},{1,4,2},{5,3,1},{5,4,2}},3));
    }
}
