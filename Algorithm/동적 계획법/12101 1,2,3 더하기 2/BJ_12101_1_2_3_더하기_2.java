import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_12101_1_2_3_더하기_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        List<String>[] arrList = new ArrayList[n+3];
        for (int i = 0; i < n + 3; i++) {
            arrList[i] = new ArrayList<>();
        }

        arrList[1].add("1");
        arrList[2].add("1+1");
        arrList[2].add("2");
        arrList[3].add("1+2");
        arrList[3].add("1+1+1");
        arrList[3].add("2+1");
        arrList[3].add("3");

        for (int i = 4; i <= n; i++) {
            for (int j = 1; j <= 3; j++) {
                for (String s : arrList[i - j]) {
                    arrList[i].add(s + "+" + j);
                }
            }
        }

        if(arrList[n].size() < k){
            System.out.println(-1);
        }else {
            Collections.sort(arrList[n]);
            System.out.println(arrList[n].get(k-1));
        }
    }
}
