import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_2138_전구와_스위치 {
    static int n;
    static int answer = Integer.MAX_VALUE;
    static boolean[] target;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        String given = br.readLine();
        String goal = br.readLine();
        boolean[] nowA = new boolean[n];
        boolean[] nowB = new boolean[n];
        target = new boolean[n];
        for (int i = 0; i < n; i++) {
            nowA[i] = given.charAt(i) != '0';
            nowB[i] = given.charAt(i) != '0';
            target[i] = goal.charAt(i) != '0';
        }

        greedy(1,0,nowA);
        greedy(1,1,onoff(0,nowB));
        System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
    }

    private static void greedy(int index, int count, boolean[] arr){
        if(index == n){
            if (arr[index - 1] == target[index - 1]) {
                answer = Math.min(answer,count);
            }
            return;
        }

        if (arr[index - 1] != target[index - 1]) {
            greedy(index + 1, count + 1, onoff(index, arr));
        }else{
            greedy(index + 1, count , arr);
        }
    }

    private static boolean[] onoff(int index, boolean[] arr){
        for (int i = index - 1; i <= index + 1; i++) {
            if (i >= 0 && i < n) {
                arr[i] = !arr[i];
            }
        }
        return arr;
    }
}
