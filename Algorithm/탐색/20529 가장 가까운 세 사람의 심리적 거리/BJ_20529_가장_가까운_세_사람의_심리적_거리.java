import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_20529_가장_가까운_세_사람의_심리적_거리 {
    static int min;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        for (int q = 0; q < t; q++) {
            min = 1000;
            int n = Integer.parseInt(br.readLine());
            String[] arr = br.readLine().strip().split(" ");
            boolean flag = false;
            for (int i = 0; i < arr.length; i++) {
                for (int j = i + 1; j < arr.length; j++) {
                    for (int k = j + 1; k < arr.length; k++) {
                        int nowDistance = distance(arr[i], arr[j]) + distance(arr[j], arr[k]) + distance(arr[i], arr[k]);
                        if (min > nowDistance) {
                            min = nowDistance;
                            if(min == 0){
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        break;
                    }
                }
                if(flag){
                    break;
                }
            }
            System.out.println(min);
        }
    }

    private static int distance(String s1, String s2){
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                result++;
            }
        }
        return result;
    }
}
