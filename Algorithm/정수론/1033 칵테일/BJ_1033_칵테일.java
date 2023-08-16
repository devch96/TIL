import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1033_칵테일 {
    static List<Node>[] arrList;
    static long lcm;
    static boolean[] visited;
    static long[] arr;
    static class Node{
        int b;
        int p;
        int q;
        public Node(int b, int p, int q){
            this.b = b;
            this.p = p;
            this.q = q;
        }

        public int getB() {
            return b;
        }

        public int getP() {
            return p;
        }

        public int getQ() {
            return q;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        arrList = new ArrayList[n];
        arr = new long[n];
        visited = new boolean[n];
        lcm = 1;
        for (int i = 0; i < n; i++) {
            arrList[i] = new ArrayList<>();
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            int q = Integer.parseInt(st.nextToken());

            arrList[a].add(new Node(b, p, q));
            arrList[b].add(new Node(a, q, p));
            lcm *= (p*q / gcd(p,q));
        }
        arr[0] = lcm;
        dfs(0);
        long mgcd = arr[0];
        for (int i = 1; i < n; i++) {
            mgcd = gcd(mgcd, arr[i]);
        }
        for (int i = 0; i < n; i++) {
            bw.write(arr[i] / mgcd + " ");
        }
        bw.flush();
        bw.close();
    }

    private static long gcd(long p, long q) {
        if(q == 0){
            return p;
        }else{
            return gcd(q, p % q);
        }
    }

    private static void dfs(int node){
        visited[node] = true;
        for (Node i : arrList[node]) {
            int nextNode = i.getB();
            if (!visited[nextNode]) {
                arr[nextNode] = arr[node] * i.getQ() / i.getP();
                dfs(nextNode);
            }
        }
    }
}
