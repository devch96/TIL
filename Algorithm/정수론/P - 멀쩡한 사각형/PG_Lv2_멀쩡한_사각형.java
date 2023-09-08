public class PG_Lv2_멀쩡한_사각형 {
    public static long solution(int w, int h){
        long answer = 0;
        double line = (double) -h / w;
        for (int i = 1; i < w; i++) {
            answer += (int) (line * i + h) * 2;
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(solution(8,12));
    }
}
