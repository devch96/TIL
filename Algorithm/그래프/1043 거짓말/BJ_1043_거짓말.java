import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_1043_거짓말 {
    static int[] parent;
    static int[] trueP;
    static List<Integer>[] party;
    static int result;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int t = Integer.parseInt(st.nextToken());

        result = 0;
        trueP = new int[t];
        for (int i = 0; i < t; i++) {
            trueP[i] = Integer.parseInt(st.nextToken());
        }

        party = new ArrayList[m];
        for (int i = 0; i < m; i++) {
            party[i] = new ArrayList<>();
            st = new StringTokenizer(br.readLine());
            int partySize = Integer.parseInt(st.nextToken());
            for (int j = 0; j < partySize; j++) {
                party[i].add(Integer.parseInt(st.nextToken()));
            }
        }

        parent = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < m; i++) {
            int firstPeople = party[i].get(0);
            for (int j = 1; j < party[i].size(); j++) {
                union(firstPeople, party[i].get(j));
            }
        }

        for (int i = 0; i < m; i++) {
            boolean isPossible = true;
            int cur = party[i].get(0);
            for (int j = 0; j < trueP.length; j++) {
                if (check(cur, trueP[j])) {
                    isPossible = false;
                    break;
                }
            }
            if(isPossible){
                result++;
            }
        }
        System.out.println(result);
    }

    private static void union(int a, int b){
        a = find(a);
        b = find(b);
        if (a != b) {
            parent[b] = a;
        }
    }

    private static int find(int a){
        if (a == parent[a]) {
            return a;
        }else{
            return parent[a] = find(parent[a]);
        }
    }

    private static boolean check(int a, int b){
        a = find(a);
        b = find(b);
        return a==b;
    }
}
