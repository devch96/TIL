import java.io.*;
import java.util.PriorityQueue;

public class BJ_11286_절댓값_힙 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(br.readLine());
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1,o2) -> {
            int o1_abs = Math.abs(o1);
            int o2_abs = Math.abs(o2);
            if (o1_abs == o2_abs) {
                return o1 > o2 ? 1 : -1;
            }else{
                return o1_abs - o2_abs;
            }
        });
        for (int i = 0; i < n; i++) {
            int request = Integer.parseInt(br.readLine());
            if (request == 0){
                if(queue.isEmpty()){
                    bw.write("0");
                    bw.flush();
                    bw.newLine();
                }else{
                    bw.write(String.valueOf(queue.poll()));
                    bw.flush();
                    bw.newLine();
                }
            }else{
                queue.add(request);
            }
        }
    }
}
