import java.io.*;

public class BJ_2720_세탁소_사장_동혁 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int change = Integer.parseInt(br.readLine());
            bw.write(calculateCoinCount(change).toString());
            bw.newLine();
            bw.flush();
        }
    }

    private static StringBuilder calculateCoinCount(int change){
        StringBuilder sb = new StringBuilder();
        int quarter = 25;
        int dime = 10;
        int nickel = 5;
        int penny = 1;
        sb.append(change/quarter+" ");
        change %= quarter;
        sb.append(change/dime+" ");
        change %= dime;
        sb.append(change/nickel+" ");
        change %= nickel;
        sb.append(change/penny);
        return sb;
    }
}
