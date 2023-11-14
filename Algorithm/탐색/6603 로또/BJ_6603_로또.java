import java.io.*;
import java.util.StringTokenizer;

public class BJ_6603_로또 {
    static int k;
    static int[] sequence;
    static int[] answer;
    static StringBuilder sb;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        while(true){
            StringTokenizer st = new StringTokenizer(br.readLine());
            k = Integer.parseInt(st.nextToken());
            if (k == 0) {
                break;
            }
            sb = new StringBuilder();
            sequence = new int[k];
            answer = new int[6];
            for (int i = 0; i < k; i++) {
                sequence[i] = Integer.parseInt(st.nextToken());
            }
            backtrack(0,0);
            bw.write(sb.toString());
            bw.flush();
            System.out.println();
        }
    }

    private static void backtrack(int depth, int start){
        if(depth == 6){
            for (int i = 0; i < 6; i++) {
                sb.append(answer[i]).append(" ");
            }
            sb.append("\n");
            return;
        }

        for (int i = start; i < k; i++) {
            answer[depth] = sequence[i];
            backtrack(depth+1, i+1);
        }
    }
}
