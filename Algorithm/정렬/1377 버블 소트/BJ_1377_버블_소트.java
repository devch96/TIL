import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class BJ_1377_버블_소트 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(br.readLine());
        Node[] arr = new Node[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Node(i, Integer.parseInt(br.readLine()));
        }
        Arrays.sort(arr, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.value - o2.value;
            }
        });
        int max = 0;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, arr[i].index - i);
        }
        System.out.println(max+1);
    }
}
class Node {
    int index;
    int value;

    public Node(int index, int value){
        this.index = index;
        this.value = value;
    }
}
