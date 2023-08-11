import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1940_주몽 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());
        int count = 0;
        List<Integer> ingredient = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            ingredient.add(Integer.parseInt(st.nextToken()));
        }
        Collections.sort(ingredient);
        int left_idx = 0;
        int right_idx = ingredient.size()-1;
        while (left_idx < right_idx) {
            int leftIngredient = ingredient.get(left_idx);
            int rightIngredient = ingredient.get(right_idx);
            int tempSum = leftIngredient + rightIngredient;
            if(tempSum == m){
                count++;
                left_idx++;
                right_idx--;
            } else if (tempSum < m) {
                left_idx++;
            } else{
                right_idx--;
            }
        }
        bw.write(String.valueOf(count));
        bw.flush();
    }
}
