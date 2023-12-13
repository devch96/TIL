import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_2109_순회강연 {
    static class Lecture {
        int day;
        int money;

        public Lecture(int day, int money) {
            this.day = day;
            this.money = money;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        Lecture[] Lectures = new Lecture[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int p = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());

            Lectures[i] = new Lecture(d,p);
        }

        Arrays.sort(Lectures, (p1, p2) -> (p1.money == p2.money) ? p2.day - p1.day : p2.money - p1.money);

        int ans = 0;
        boolean[] check = new boolean[10001];
        for (int i = 0; i < N; i++) {
            for (int j = Lectures[i].day; j >= 1; j--) {
                if (!check[j]) {
                    check[j] = true;
                    ans += Lectures[i].money;
                    break;
                }
            }
        }
        System.out.println(ans);
    }
}
