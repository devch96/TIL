import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BJ_12970_AB {
	static int n;
	static int k;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		int aLength = countA();
		int bLength = n - aLength;
		String[] result = new String[n];
		if (aLength != -1 && k != 0) {
			Arrays.fill(result,"B");
			for (int i = 0; i < aLength - 1; i++) {
				result[i] = "A";
			}
			int c = (aLength - 1) * bLength;
			int r = k - c;
			result[n-1-r] = "A";
		} else if (k == 0) {
			Arrays.fill(result,"B");
		}
		System.out.println(aLength == -1 ? -1 : String.join("", result));
	}

	private static int countA(){
		int a = 1;
		int b = n-1;
		while(a * b < k){
			if(b < 0){
				return -1;
			}
			a++;
			b--;
		}
		return a;
	}
}
