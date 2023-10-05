public class PG_Lv2_유사_칸토어_비트열 {

    public static int solution(int n, long l, long r){
        int answer = 0;
        for (long i = l - 1; i < r; i++) {
            if(isOne(i)){
                answer++;
            }
        }
        return answer;
    }

    private static boolean isOne(long i){
        if(i % 5 == 2){
            return false;
        }
        if (i < 5) {
            return true;
        }
        return isOne(i/5);
    }

    public static void main(String[] args) {
        System.out.println(solution(2,4,17));
    }
}
