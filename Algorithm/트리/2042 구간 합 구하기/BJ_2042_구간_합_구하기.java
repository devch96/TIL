import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_2042_구간_합_구하기 {
    static long[] tree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int treeHeight = 0;
        int len = n;
        while (len != 0) {
            len /= 2;
            treeHeight++;
        }
        int treeSize = (int) Math.pow(2, treeHeight + 1);
        int leafNodeStartIndex = treeSize/2 - 1;
        tree = new long[treeSize + 1];

        for (int i = leafNodeStartIndex + 1; i <= leafNodeStartIndex + n; i++) {
            tree[i] = Long.parseLong(br.readLine());
        }
        setTree(treeSize - 1);

        for (int i = 0; i < m + k; i++) {
            st = new StringTokenizer(br.readLine());
            long a = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            long e = Long.parseLong(st.nextToken());
            if (a == 1) {
                changeVal(leafNodeStartIndex + s, e);
            } else if (a == 2) {
                s += leafNodeStartIndex;
                e += leafNodeStartIndex;
                System.out.println(getSum(s, (int) e));
            }else{
                return;
            }
        }
    }

    private static void setTree(int i) {
        while(i != 1){
            tree[i / 2] += tree[i];
            i--;
        }
    }

    private static void changeVal(int index, long val) {
        long diff = val - tree[index];
        while (index > 0) {
            tree[index] += diff;
            index /= 2;
        }
    }

    private static long getSum(int s, int e){
        long partSum = 0;
        while (s <= e) {
            if(s%2 == 1){
                partSum += tree[s];
                s++;
            }
            if (e % 2 == 0) {
                partSum += tree[e];
                e--;
            }
            s = s/2;
            e = e/ 2;
        }
        return partSum;
    }

}
