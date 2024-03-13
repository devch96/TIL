public class PG_Lv3_기지국_설치 {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;
        answer += calculate(1, Math.max(0, stations[0] - w - 1), 2*w+1);
        for(int i = 1; i < stations.length; i++) {
            answer += calculate(stations[i-1] + w + 1, stations[i] - w - 1, 2*w+1);
        }
        answer += calculate(stations[stations.length-1]+w+1, n, 2*w+1);
        return answer;
    }
    private int calculate(int start, int end, int w) {
        int num = end-start+1;
        if (num <= 0) {
            return 0;
        }
        if (num % w == 0) {
            return (num / w);
        } else {
            return (num /w) + 1;
        }
    }

    public static void main(String[] args) {
        PG_Lv3_기지국_설치 p = new PG_Lv3_기지국_설치();
        p.solution(16,new int[] {9}, 2);
    }
}
