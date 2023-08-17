import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_1219_오민식의_고민 {
    static class Edge{
        int start, end, price;
        public Edge(int start, int end, int price){
            this.start = start;
            this.end = end;
            this.price = price;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int startCity = Integer.parseInt(st.nextToken());
        int endCity = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        Edge[] edges = new Edge[m];
        long[] distance = new long[n];
        long[] cityIncome = new long[n];

        Arrays.fill(distance, Long.MIN_VALUE);
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            int price = Integer.parseInt(st.nextToken());
            edges[i] = new Edge(start, end, price);
        }
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            cityIncome[i] = Long.parseLong(st.nextToken());
        }
        distance[startCity] = cityIncome[startCity];

        for (int i = 0; i <= n + 100; i++) {
            for (int j = 0; j < m; j++) {
                Edge edge = edges[j];
                int start = edge.start;
                int end = edge.end;
                int price = edge.price;
                if(distance[start] == Long.MIN_VALUE) {
                    continue;
                } else if (distance[start] == Long.MAX_VALUE) {
                    distance[end] = Long.MAX_VALUE;
                } else if (distance[end] < distance[start] + cityIncome[end] - price) {
                    distance[end] = distance[start] + cityIncome[end] - price;
                    if( i >= n-1){
                        distance[end] = Long.MAX_VALUE;
                    }
                }
            }
        }
        if (distance[endCity] == Long.MIN_VALUE) {
            System.out.println("gg");
        } else if (distance[endCity] == Long.MAX_VALUE) {
            System.out.println("Gee");
        }else{
            System.out.println(distance[endCity]);
        }
    }
}
