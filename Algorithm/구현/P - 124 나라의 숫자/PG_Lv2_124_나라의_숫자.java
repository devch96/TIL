public class PG_Lv2_124_나라의_숫자 {
    public static String solution(int n) {
        String[] numbers = {"4", "1", "2"};
        StringBuilder answer = new StringBuilder();

        int num = n;

        while(num > 0){
            int remainder = num % 3;
            num /= 3;

            if(remainder == 0) num--;

            answer.append(numbers[remainder]);
        }

        return answer.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println(solution(1));
        System.out.println(solution(2));
        System.out.println(solution(3));
        System.out.println(solution(4));
        System.out.println(solution(5));
        System.out.println(solution(6));
        System.out.println(solution(7));
        System.out.println(solution(8));
        System.out.println(solution(9));
        System.out.println(solution(10));
        System.out.println(solution(11));
    }
}
