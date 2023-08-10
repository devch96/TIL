import java.io.*;
import java.util.*;

public class BJ_1246_온라인_판매 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        List<Integer> expectedPrices = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            expectedPrices.add(Integer.parseInt(br.readLine()));
        }
        Collections.sort(expectedPrices,Comparator.reverseOrder());
        bw.write(getPriceAndMaxIncome(expectedPrices, n));
        bw.flush();
    }

    private static String getPriceAndMaxIncome(List<Integer> expectedPrices, int totalEggCount){
        int price = 0;
        int income = 0;
        for (int idx = 0; idx < Math.min(expectedPrices.size(), totalEggCount); idx++) {
            if(check(income, expectedPrices.get(idx) * (idx+1))){
                income = expectedPrices.get(idx) * (idx + 1);
                price = expectedPrices.get(idx);
            }
        }
        String result = price + " " + income;
        return result;
    }

    private static boolean check(int beforeIncome, int newIncome) {
        return beforeIncome <= newIncome;
    }
}
