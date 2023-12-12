import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_1202_보석_도둑 {
    static class Jewel implements Comparable<Jewel>{
        int weight;
        int value;

        public Jewel(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }


        @Override
        public int compareTo(Jewel o) {
            if(this.weight == o.weight){
                return Integer.compare(o.value, this.value);
            }
            return Integer.compare(this.weight, o.weight);
        }
    }
    static int n;

    static int k;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        Jewel[] jewels = new Jewel[n];
        int[] bags = new int[k];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            jewels[i] = new Jewel(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }
        Arrays.sort(jewels);
        for (int i = 0; i < k; i++) {
            bags[i] = Integer.parseInt(br.readLine());
        }
        Arrays.sort(bags);
        Queue<Integer> priceQueue = new PriorityQueue<>(Comparator.reverseOrder());
        long answer = 0;
        for (int i = 0, j = 0; i < k; i++) {
            while(j < n && jewels[j].weight <= bags[i]){
                priceQueue.offer(jewels[j++].value);
            }
            if (!priceQueue.isEmpty()) {
                answer += priceQueue.poll();
            }
        }
        System.out.println(answer);
    }
}
