import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1991_트리_순회 {
    static int[][] tree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        tree = new int[26][2];

        for (int i = 0; i < n; i++) {
            String[] temp = br.readLine().trim().split(" ");
            int node = temp[0].charAt(0) - 'A';
            char left = temp[1].charAt(0);
            char right = temp[2].charAt(0);
            if(left == '.'){
                tree[node][0] = -1;
            }else{
                tree[node][0] = left - 'A';
            }
            if (right == '.') {
                tree[node][1] = -1;
            }else{
                tree[node][1] = right - 'A';
            }
        }

        preOrder(0);
        System.out.println();
        inOrder(0);
        System.out.println();
        postOrder(0);
    }

    private static void preOrder(int now) {
        if (now == -1) {
            return;
        }
        System.out.print((char) (now + 'A'));
        preOrder(tree[now][0]);
        preOrder(tree[now][1]);
    }

    private static void inOrder(int now) {
        if (now == -1) {
            return;
        }
        inOrder(tree[now][0]);
        System.out.print((char) (now + 'A'));
        inOrder(tree[now][1]);
    }
    private static void postOrder(int now) {
        if (now == -1) {
            return;
        }
        postOrder(tree[now][0]);
        postOrder(tree[now][1]);
        System.out.print((char) (now + 'A'));
    }
}
