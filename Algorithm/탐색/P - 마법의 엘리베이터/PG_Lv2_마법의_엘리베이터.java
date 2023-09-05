import java.util.ArrayDeque;
import java.util.Deque;

public class PG_Lv2_마법의_엘리베이터 {
    public static int solution(int storey) {
        int answer = 0;

        while(storey > 0) {
            int num = storey % 10;
            storey /= 10;

            if (num > 5) {
                answer += 10 - num;
                storey++;

            } else if (num < 5) {
                answer += num;
            } else if (storey % 10 >= 5) {
                answer += 5;
                storey++;

            } else {
                answer += 5;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(solution(2554));
    }
}
