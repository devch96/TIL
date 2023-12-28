import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BJ_11652_카드 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = Integer.parseInt(br.readLine());
        Map<Long, Integer> map = new HashMap<>();
        Long input;
        int max=1;

        for(int i=0;i<num;i++) {
            input=Long.parseLong(br.readLine());
            if(map.containsKey(input)) {
                int val = map.get(input);
                if(val+1>max) {
                    max=val+1;
                }
                map.put(input, val+1);
            } else {
                map.put(input, 1);
            }
        }
        List<Long> list = new ArrayList<>();
        for(Map.Entry<Long, Integer> m : map.entrySet()) {
            if(m.getValue()==max)
                list.add(m.getKey());
        }
        Collections.sort(list);
        System.out.println(list.get(0));
    }
}
