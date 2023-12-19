import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BJ_12904_A와_B {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String start = br.readLine();
		StringBuilder target = new StringBuilder(br.readLine());
		int startLength = start.length();
		int targetLength = target.length();
		while (startLength != targetLength) {
			if (target.charAt(targetLength - 1) == 'A') {
				target.deleteCharAt(targetLength - 1);
				targetLength--;
			}else{
				target.deleteCharAt(targetLength - 1);
				target.reverse();
				targetLength--;
			}
		}
		if (start.contentEquals(target)) {
			System.out.println(1);
		}else{
			System.out.println(0);
		}
	}
}
