public class PG_Lv3_스티커_모으기_2 {
    public int solution(int sticker[]) {
        int answer = 0;
        int length = sticker.length;
        if(length == 1){
            return sticker[0];
        }else if(length == 2) {
            return Math.max(sticker[0],sticker[1]);
        }

        int[] tmpSticker = new int[length];
        int[] tmpSticker2 = new int[length];
        for(int i = 0; i < length; i++) {
            tmpSticker[i] = sticker[i];
        }
        tmpSticker[length-1] = 0;

        for(int i = 0; i < length-1; i++) {
            tmpSticker2[i] = sticker[i+1];
        }
        tmpSticker2[length-1] = 0;

        int[] dp = new int[length];
        int[] dp2 = new int[length];

        dp[0] = tmpSticker[0];
        dp[1] = Math.max(dp[0], tmpSticker[1]);

        for(int i=0;i<length-2;i++){
            dp[i+2] = Math.max(dp[i] + tmpSticker[i+2], dp[i+1]);
        }


        dp2[0] = tmpSticker2[0];
        dp2[1] = Math.max(dp2[0], tmpSticker2[1]);

        for(int i=0;i<length-2;i++){
            dp2[i+2] = Math.max(dp2[i] + tmpSticker2[i+2], dp2[i+1]);
        }
        return Math.max(dp[length-1], dp2[length-1]);
    }

    public static void main(String[] args) {
        PG_Lv3_스티커_모으기_2 p = new PG_Lv3_스티커_모으기_2();
        p.solution(new int[]{1, 3, 2, 5, 4});
    }
}
