import java.io.*;
import java.util.StringTokenizer;

public class BJ_2268_수들의_합_7 {
    static long[] tree;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        tree = new long[n * 4];

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            if (a == 0) {
                if (b > c) {
                    int temp = b;
                    b = c;
                    c = temp;
                }
                sb.append(sum(1, n, 1, b, c) + "\n");
            } else if (a == 1) {
                update(1, n, 1, b, c);
            }
        }

        bw.write(sb.toString());
        bw.flush();
    }

    private static long sum(int start, int end, int node, int left, int right){
        if(left > end || right < start){
            return 0;
        }
        if (left <= start && right >= end) {
            return tree[node];
        }
        int mid = (start + end)/2;
        return sum(start, mid, node*2, left, right) + sum(mid+1, end, node*2+1, left, right);
    }

    private static long update(int start, int end, int node, int index, int value){
        if(index < start || index > end){
            return tree[node];
        }
        if (start == end){
            return tree[node] = value;
        }
        int mid = (start + end)/2;
        return tree[node] = update(start, mid, node*2, index, value) + update(mid+1, end, node*2+1, index, value);
    }
}
