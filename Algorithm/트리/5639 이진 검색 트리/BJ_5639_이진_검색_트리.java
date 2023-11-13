import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_5639_이진_검색_트리 {
    static class Node{
        int num;
        Node left;
        Node right;

        Node(int num){
            this.num = num;
        }

        void insert(int n){
            if(this.num > n){
                if(this.left == null){
                    this.left = new Node(n);
                }else{
                    this.left.insert(n);
                }
            }else{
                if(this.right == null){
                    this.right = new Node(n);
                }else{
                    this.right.insert(n);
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Node root = new Node(Integer.parseInt(br.readLine()));
        while(true){
            String input = br.readLine();
            if (input == null || input.isBlank()) {
                break;
            }
            root.insert(Integer.parseInt(input));
        }
        postOrder(root);
    }

    private static void postOrder(Node node){
        if(node == null){
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.num);
    }
}
