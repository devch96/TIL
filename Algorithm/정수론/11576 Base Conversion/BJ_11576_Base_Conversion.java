import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_11576_Base_Conversion {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        int[] arr = new int[m];
        for (int i = 0; i < m; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        int[] reverseOutputArr = baseConversion(a, b, arr);
        for (int i = reverseOutputArr.length - 1; i >= 0; i--) {
            System.out.print(reverseOutputArr[i] + " ");
        }

    }

    private static int[] baseConversion(int inputBase, int outputBase, int[] arr){
        int[] result = new int[arr.length];
        int decimalNum = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            decimalNum += (int) (arr[i] * Math.pow(inputBase, arr.length - 1 - i));
        }
        return conversion(outputBase, decimalNum);
    }

    private static int[] conversion(int base, int num){
        List<Integer> result = new ArrayList<>();
        while(num != 0){
            result.add(num % base);
            num /= base;
        }
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
