import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_2923_숫자_게임 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        int[] firstArray = new int[101];
        int[] secondArray = new int[101];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            firstArray[a]++;
            secondArray[b]++;
            int[] copiedFirstArray = Arrays.copyOfRange(firstArray, 0 , 101);
            int[] copiedSecondArray = Arrays.copyOfRange(secondArray, 0 , 101);
            int first = 0;
            int second = 100;
            int answer = 0;
            while (true) {
                if(copiedFirstArray[first] == 0){
                    first++;
                    if (first > 100) {
                        break;
                    }
                    continue;
                }
                if (copiedSecondArray[second] == 0) {
                    second--;
                    if (second < 0) {
                        break;
                    }
                    continue;
                }
                answer = Math.max(answer, first + second);
                int temp = Math.min(copiedFirstArray[first], copiedSecondArray[second]);
                copiedFirstArray[first] -= temp;
                copiedSecondArray[second] -= temp;
            }
            bw.write(String.valueOf(answer));
            bw.flush();
            bw.newLine();
        }
    }
}
