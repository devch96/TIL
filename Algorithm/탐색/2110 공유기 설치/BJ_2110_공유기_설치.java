import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_2110_공유기_설치 {
    static int[] houses;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        houses = new int[n];
        for (int i = 0; i < n; i++) {
            houses[i] = Integer.parseInt(br.readLine());
        }

        Arrays.sort(houses);

        int low = 1;
        int high = houses[n-1] - houses[0] + 1;
        while(low < high){
            int mid = low + (high - low) / 2;
            if(canInstall(mid) < m){
                high = mid;
            }else{
                low = mid + 1;
            }
        }
        System.out.println(low - 1);
    }

    private static int canInstall(int distance) {
        int count = 1;
        int lastLocate = houses[0];
        for (int i = 1; i <houses.length; i++) {
            int locate = houses[i];
            if (locate - lastLocate >= distance) {
                count++;
                lastLocate = locate;
            }
        }
        return count;
    }
}
