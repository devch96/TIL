import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_10610_30 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine().strip();
        int strlen = str.length();
        int[] numCount = new int[10];
        long total = 0;
        for (int i = 0; i < strlen; i++) {
            int num = str.charAt(i) - '0';
            numCount[num]++;
            total+=num;
        }
        if(numCount[0] == 0 || total % 3 != 0){
            System.out.println("-1");
            return;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 9; i >= 0 ; i--) {
            while(numCount[i]-- > 0){
                sb.append(i);
            }
        }
        System.out.println(sb);

    }

}
