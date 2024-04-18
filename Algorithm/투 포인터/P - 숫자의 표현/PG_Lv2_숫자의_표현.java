public class PG_Lv2_숫자의_표현 {
    public int solution(int n) {
        int left = 1;
        int right = 1;
        int sum = 1;
        int answer = 0;
        while(left <= n) {
            if(sum < n) {
                sum+= ++right;
            } else{
                if(sum == n) {
                    answer++;
                }
                sum-= left++;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        PG_Lv2_숫자의_표현 p = new PG_Lv2_숫자의_표현();
        p.solution(15);
    }
}
