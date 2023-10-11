import java.io.*;
import java.util.*;

public class BJ_17299_오등큰수 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int[] count = new int[1000001];
        for (int i = 0; i < n; i++) {
            count[arr[i]]++;
        }
        int[] answer = new int[n];
        Arrays.fill(answer,-1);

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && count[arr[stack.peek()]] < count[arr[i]]) {
                answer[stack.pop()] = arr[i];
            }
            stack.push(i);
        }
        for(int i = 0; i < n; i++)
            bw.write(answer[i]+" ");
        bw.flush();
    }
}
