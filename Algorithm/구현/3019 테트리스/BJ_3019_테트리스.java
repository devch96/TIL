import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_3019_테트리스 {
    static class Block {
        int size;
        int[] shape;

        public Block(int size, int[] shape) {
            this.size = size;
            this.shape = shape;
        }
    }
    static int c;
    static int p;
    static int answer;
    static int[] tetris;
    static List<Block> block;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        c = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());
        tetris = new int[c];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < c; i++) {
            tetris[i] = Integer.parseInt(st.nextToken());
        }
        getBlockShape();
        for (int i = 0; i < block.size(); i++) {
            Block b = block.get(i);
            for (int j = 0; j < c - b.size + 1; j++) {
                boolean canDrop = true;
                int firstNum = getMin(j, b.size);
                for (int k = j; k < j + b.size; k++) {
                    int idx = k-j;
                    int floorNum = tetris[k] - firstNum;
                    if (b.shape[idx] != floorNum) {
                        canDrop = false;
                        break;
                    }
                }
                if (canDrop) {
                    answer++;
                }
            }
        }
        bw.write(String.valueOf(answer));
        bw.flush();
    }
    private static void getBlockShape() {
        block = new ArrayList<>();

        switch (p) {
            case 1:
                block.add(new Block(1, new int[]{0}));
                block.add(new Block(4, new int[]{0, 0, 0, 0}));
                break;
            case 2:
                block.add(new Block(2, new int[]{0, 0}));
                break;
            case 3:
                block.add(new Block(3, new int[]{0, 0, 1}));
                block.add(new Block(2, new int[]{1, 0}));
                break;
            case 4:
                block.add(new Block(3, new int[]{1, 0, 0}));
                block.add(new Block(2, new int[]{0, 1}));
                break;
            case 5:
                block.add(new Block(3, new int[]{0, 0, 0}));
                block.add(new Block(3, new int[]{1, 0, 1}));
                block.add(new Block(2, new int[]{0, 1}));
                block.add(new Block(2, new int[]{1, 0}));
                break;
            case 6:
                block.add(new Block(3, new int[]{0, 0, 0}));
                block.add(new Block(3, new int[]{0, 1, 1}));
                block.add(new Block(2, new int[]{0, 0}));
                block.add(new Block(2, new int[]{2, 0}));
                break;
            case 7:
                block.add(new Block(3, new int[]{0, 0, 0}));
                block.add(new Block(3, new int[]{1, 1, 0}));
                block.add(new Block(2, new int[]{0, 2}));
                block.add(new Block(2, new int[]{0, 0}));
                break;
        }
    }

    private static int getMin(int start, int size) {
        int min = tetris[start];
        for (int i = start; i < start + size; i++) {
            min = Math.min(min, tetris[i]);
        }
        return min;
    }
}
