import java.io.*;
import java.util.*;

public class BJ_1933_스카이라인 {
    static class Building{
        int x;
        int h;

        public Building(int x, int h) {
            this.x = x;
            this.h = h;
        }
    }

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        List<Building> buildings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int lx = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int rx = Integer.parseInt(st.nextToken());
            buildings.add(new Building(lx, h));
            buildings.add(new Building(rx, -h));
        }

        buildings.sort(((o1, o2) -> o1.x == o2.x ? o2.h - o1.h : o1.x - o2.x));
        TreeMap<Integer, Integer> tm = new TreeMap<>((o1, o2) -> o2 - o1);
        List<Building> answer = new ArrayList<>();
        for (int i = 0; i < buildings.size(); i++) {
            Building now = buildings.get(i);
            if (now.h > 0) {
                tm.put(now.h, tm.getOrDefault(now.h, 0) + 1);
            }else{
                int key = -now.h;
                int val = tm.get(key);
                if(val == 1){
                    tm.remove(key);
                }else{
                    tm.put(key, val - 1);
                }
            }
            if(tm.size() == 0){
                answer.add(new Building(now.x, 0));
                continue;
            }
            answer.add(new Building(now.x, tm.firstKey()));
        }
        bw.write(answer.get(0).x + " " + answer.get(0).h + " ");
        int prev = answer.get(0).h;
        for (int i = 1; i < answer.size(); i++) {
            if (prev != answer.get(i).h) {
                bw.write(answer.get(i).x + " " + answer.get(i).h + " ");
                prev = answer.get(i).h;
            }
        }
        bw.write("\n");
        bw.flush();
    }
}
