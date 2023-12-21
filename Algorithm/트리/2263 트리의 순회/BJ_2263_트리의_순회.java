import java.io.*;
import java.util.StringTokenizer;

public class BJ_2263_트리의_순회 {
    static int n;
    static int[] in;
    static int[] post;
    static int[] pre;
    static int index;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        n = Integer.parseInt(br.readLine());

        in = new int[n];
        post = new int[n];
        pre = new int[n];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            in[i] = Integer.parseInt(st.nextToken());
        }
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            post[i] = Integer.parseInt(st.nextToken());
        }

        getPreOrder(0, n - 1, 0, n - 1);
        for(int n : pre){
            bw.write(n + " ");
        }
        bw.flush();
    }

    private static void getPreOrder(int inStart, int inEnd, int postStart, int postEnd){
        if (inStart <= inEnd && postStart <= postEnd) {
            pre[index++] = post[postEnd];
            int position = inStart;
            for (int i = inStart; i <= inEnd; i++) {
                if(in[i] == post[postEnd]){
                    position = i;
                    break;
                }
            }
            getPreOrder(inStart, position-1, postStart, postStart + position - inStart -1);
            getPreOrder(position+1, inEnd, postStart + position - inStart, postEnd-1);
        }
    }
}
