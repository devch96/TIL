import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_11505_구간_곱_구하기 {
    static long[] tree;
    static int mod;

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
        int leftNodeStartIndex = treeSize / 2 - 1;
        tree = new long[treeSize + 1];
        Arrays.fill(tree, 1);
        mod = 1_000_000_007;

        for (int i = leftNodeStartIndex + 1; i <= leftNodeStartIndex + n; i++) {
            tree[i] = Long.parseLong(br.readLine());
        }
        setTree(treeSize - 1);

        for (int i = 0; i < m + k; i++) {
            st = new StringTokenizer(br.readLine());
            long a = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            long e = Integer.parseInt(st.nextToken());
            if (a == 1) {
                changeVal(leftNodeStartIndex + s, e);
            } else if (a == 2) {
                s += leftNodeStartIndex;
                e += leftNodeStartIndex;
                System.out.println(getMul(s,(int) e));
            }else{
                return;
            }
        }
    }

    private static void changeVal(int index, long val) {
        tree[index] = val;
        while (index > 1) {
            index /= 2;
            tree[index] = tree[index * 2] % mod * tree[index * 2 + 1] % mod;
        }
    }

    private static long getMul(int s, int e) {
        long partMul = 1;
        while (s <= e) {
            if (s % 2 == 1) {
                partMul = partMul * tree[s] % mod;
                s++;
            }
            if (e % 2 == 0) {
                partMul = partMul * tree[e] % mod;
                e--;
            }
            s /= 2;
            e /= 2;
        }
        return partMul;
    }

    private static void setTree(int i) {
        while (i != 1) {
            tree[i / 2] = tree[i / 2] * tree[i] % mod;
            i--;
        }
    }
}
