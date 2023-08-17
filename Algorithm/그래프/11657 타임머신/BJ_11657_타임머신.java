import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_11657_타임머신 {
    static class Bus{
        int start;
        int destination;
        int time;

        Bus(int start, int destination, int time) {
            this.start = start;
            this.destination = destination;
            this.time = time;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        Bus[] buses = new Bus[m + 1];
        long[] distance = new long[n + 1];


        Arrays.fill(distance,Integer.MAX_VALUE);

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int destination = Integer.parseInt(st.nextToken());
            int time = Integer.parseInt(st.nextToken());
            buses[i] = new Bus(start, destination, time);
        }

        distance[1] = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Bus bus = buses[j];
                if (distance[bus.start] != Integer.MAX_VALUE && distance[bus.destination] > distance[bus.start] + bus.time) {
                    distance[bus.destination] = distance[bus.start] + bus.time;
                }
            }
        }

        boolean mCycle = false;
        for (int j = 0; j < m; j++) {
            Bus bus = buses[j];
            if (distance[bus.start] != Integer.MAX_VALUE && distance[bus.destination] > distance[bus.start] + bus.time) {
                mCycle = true;
            }
        }
        if (!mCycle) {
            for (int i = 2; i <= n; i++) {
                if (distance[i] == Integer.MAX_VALUE) {
                    System.out.println("-1");
                }else{
                    System.out.println(distance[i]);
                }
            }
        }else{
            System.out.println("-1");
        }
    }
}
