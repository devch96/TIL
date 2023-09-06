import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PG_Lv2_숫자_카드_나누기 {
    public static int solution(int[] arrayA, int[] arrayB){
        List<Integer> arrayACommonDivisor = getCommonDivisorDESC(arrayA);
        List<Integer> arrayBCommonDivisor = getCommonDivisorDESC(arrayB);

        int max = 0;
        boolean flag;
        for(int i : arrayACommonDivisor){
            if(i == 1){
                break;
            }
            flag = true;
            for(int j : arrayB){
                if (j % i == 0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                max = Math.max(max,i);
            }
        }

        for(int i : arrayBCommonDivisor){
            if(i == 1){
                break;
            }
            flag = true;
            for(int j : arrayA){
                if (j % i == 0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                max = Math.max(max,i);
            }
        }
        return max;
    }

    private static List<Integer> getCommonDivisorDESC(int[] arr){
        List<Integer> result = new ArrayList<>();
        int gcd = getArrayGCD(arr);
        for (int i = 1; i <= Math.sqrt(gcd); i++) {
            if(gcd % i == 0){
                result.add(i);
                if(i * i != gcd){
                    result.add(gcd / i);
                }
            }
        }
        result.sort(Comparator.reverseOrder());
        return result;
    }

    private static int getArrayGCD(int[] arr){
        int gcd = arr[0];
        for (int i = 1; i < arr.length; i++) {
            gcd = GCD(gcd, arr[i]);
        }
        return gcd;
    }

    private static int GCD(int x, int y){
        while(y != 0){
            int remain = x % y;
            x = y;
            y = remain;
        }
        return x;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[] {10,17}, new int[] {5,20}));
        System.out.println(solution(new int[] {10,20}, new int[] {5,17}));
        System.out.println(solution(new int[] {14,35,119}, new int[] {18,30,102}));
    }
}
