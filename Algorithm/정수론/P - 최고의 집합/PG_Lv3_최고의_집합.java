public class PG_Lv3_최고의_집합 {
    public int[] solution(int n, int s) {
        if(n > s) {
            return new int[] {-1};
        }
        int[] answer = new int[n];
        int index = 0;
        while(s > 0) {
            int value = s/n;
            answer[index] = value;
            index++;
            s -= value;
            n--;
        }
        return answer;
    }
}
