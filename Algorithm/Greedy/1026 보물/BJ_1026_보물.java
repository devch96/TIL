import java.io.*;
import java.util.*;

public class BJ_1026_보물 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        List<Integer> listA = tokenInput(st,n);
        st = new StringTokenizer(br.readLine());
        List<Integer> listB = tokenInput(st,n);
        Collections.sort(listA);
        listA.sort(Comparator.naturalOrder());
        listB.sort(Comparator.reverseOrder());
        bw.write(String.valueOf(calculateMin(listA, listB)));
        bw.flush();
    }

    private static List<Integer> tokenInput(StringTokenizer st, int loop){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < loop; i++) {
            result.add(Integer.parseInt(st.nextToken()));
        }
        return result;
    }

    private static int calculateMin(List<Integer> listA, List<Integer> listB) {
        int result = 0;
        for (int idx = 0; idx < listA.size(); idx++) {
            result += listA.get(idx) * listB.get(idx);
        }
        return result;
    }
}
