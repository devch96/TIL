import java.io.*;
import java.util.*;

public class BJ_1406_에디터 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        String str = br.readLine().strip();
        int m = Integer.parseInt(br.readLine());

        Deque<String> leftStack = new ArrayDeque<>();
        Deque<String> rightStack = new ArrayDeque<>();

        for(String s : str.split("")){
            leftStack.push(s);
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String command = st.nextToken();
            switch (command){
                case "L":
                    if (!leftStack.isEmpty()) {
                        rightStack.push(leftStack.pop());
                    }
                    break;
                case "D":
                    if (!rightStack.isEmpty()) {
                        leftStack.push(rightStack.pop());
                    }
                    break;
                case "B":
                    if (!leftStack.isEmpty()) {
                        leftStack.pop();
                    }
                    break;
                case "P":
                    String s = st.nextToken();
                    leftStack.push(s);
                    break;
            }
        }
        while (!leftStack.isEmpty()) {
            rightStack.push(leftStack.pop());
        }
        while (!rightStack.isEmpty()) {
            bw.write(rightStack.pop());
        }
        bw.flush();
    }
}
