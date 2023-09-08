public class PG_Lv2_점_찍기 {
    public static long solution(int k, int d){
        long answer = 0;
        for(int i = 0; i <= d; i+=k){
            int yMaxDistance = yPossibleDistance(i,d);
            answer += yPossibleCount(yMaxDistance, k);
        }
        return answer;
    }

    private static int yPossibleDistance(int x, int y){
        long xx = (long) Math.pow(x,2);
        long yy = (long) Math.pow(y,2);

        int result = (int) Math.sqrt(yy-xx);
        return result;
    }

    private static long yPossibleCount(int possible, int k){
        int y = possible/k;
        return y+1;
    }
}
