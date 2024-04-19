public class PG_Lv2_다음_큰_숫자 {
    public int solution(int n) {
        // input n = 11110000
        // expected output = 100000111

        // 가장 오른쪽 1인 비트 구하기
        // 11110000 -n은 2의 보수
        int rightOne = n & -n;

        // 가장 오른쪽 1인 비트와 현재 비트 더하면
        // 가장 왼쪽 비트 값 설정됨
        int nextHigherOneBit = n + rightOne;

        //
        int rightOnesPattern = n ^ nextHigherOneBit;
        rightOnesPattern = rightOnesPattern / rightOne;
        rightOnesPattern = rightOnesPattern >> 2;
        return nextHigherOneBit | rightOnesPattern;
    }

    public static void main(String[] args) {
        PG_Lv2_다음_큰_숫자 p = new PG_Lv2_다음_큰_숫자();
        System.out.println(p.solution(78));
    }
}
