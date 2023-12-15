import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BJ_1783_병든_나이트 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int n = Integer.parseInt(st.nextToken());
		int m = Integer.parseInt(st.nextToken());

		int answer = 0;
		if(n == 1){
			answer = 1;
		}
		if(n == 2){
			answer = Math.min(4, (m + 1) / 2);
		}
		if(n >= 3){
			if (m >= 7) {
				answer = m-2;
			}else{
				answer = Math.min(4, m);
			}
		}
		System.out.println(answer);
	}
}
