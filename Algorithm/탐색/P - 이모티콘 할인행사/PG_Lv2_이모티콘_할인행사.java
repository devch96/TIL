import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PG_Lv2_이모티콘_할인행사 {
    static List<int[]> priceCombinations;
    static int[] emoticonPrice;
    static int[][] usersInfo;

    public static int[] solution(int[][] users, int[] emoticons) {
        priceCombinations = new ArrayList<>();
        emoticonPrice = emoticons;
        usersInfo = users;

        for (int i = 10; i <= 40; i += 10) {
            int[] prices = new int[users.length];
            dfs(0, i, prices);
        }

        int maxCount = 0;
        int maxSum = 0;
        for(int[] prices : priceCombinations){
            int emoticonPlus = 0;
            for (int i = 0; i < usersInfo.length; i++) {
                int standard = usersInfo[i][1];
                if (prices[i] >= standard) {
                    prices[i] = 0;
                    emoticonPlus++;
                }
            }
            int sum = Arrays.stream(prices).sum();
            if (maxCount < emoticonPlus) {
                maxCount = emoticonPlus;
                maxSum = sum;
            } else if (maxCount == emoticonPlus) {
                maxSum = Math.max(maxSum, sum);
            }
        }
        return new int[]{maxCount, maxSum};
    }

    private static void dfs(int index, int discountRate, int[] prices){
        if (index == emoticonPrice.length) {
            return;
        }
        int[] clone = prices.clone();
        for (int i = 0; i < usersInfo.length; i++) {
            if(discountRate < usersInfo[i][0]){
                continue;
            }
            int discountPrice = emoticonPrice[index] * (100 - discountRate) / 100;
            clone[i] += discountPrice;
        }

        for (int i = 10; i <= 40; i += 10) {
            dfs(index+1, i, clone);
        }
        if (index == emoticonPrice.length - 1) {
            priceCombinations.add(clone);
        }
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new int[][]{{40, 2900}, {23, 10000}, {11, 5200}, {5, 5900}, {40, 3100}, {27, 9200}, {32, 6900}}, new int[]{1300, 1500, 1600, 4900})));
    }
}
