import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class BJ_1138_다음_팰린드롬_수 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String n = br.readLine().strip();
        System.out.println(palindrome(n));
    }

    private static String palindrome(String num){
        if(num.length() == 1 || num.equals("10")) {
            int n = Integer.parseInt(num);
            if(n < 9) {
                n++;
                return Integer.toString(n);
            } else {
                return "11";
            }
        }
        String r_num = "";
        if(num.length() % 2 == 1) {
            r_num += num.substring(0, num.length() / 2) + num.substring(num.length() / 2, num.length() / 2 + 1) + num.substring(num.length() / 2 + 1, num.length());
        } else {
            r_num += num.substring(0, num.length() / 2) + num.substring(num.length() / 2, num.length());
        }
        if(r_num.compareTo(num) > 0) {
            return r_num;
        }else {
            String result;
            BigInteger int_num = new BigInteger(num);
            int_num = int_num.add(BigInteger.ONE);
            num = int_num.toString();
            int half = num.length() / 2;
            if(num.length() % 2 == 1) {
                String left = num.substring(0, half);
                String mid = num.substring(half, half + 1);
                String right = num.substring(half + 1, num.length());
                if(reverseString(left).compareTo(right) >= 0) {
                    result = left + mid + reverseString(left);
                } else {
                    if(mid.equals("9")) {
                        mid = "0";
                        int prev_length = left.length();
                        BigInteger bnum = new BigInteger(left);
                        bnum = bnum.add(BigInteger.ONE);
                        left = bnum.toString();
                        if(left.length() > prev_length) {
                            result = left + reverseString(left);
                        } else {
                            result = left + mid + reverseString(left);
                        }
                    } else {
                        result = left + Integer.toString(Integer.parseInt(mid) + 1) + reverseString(left);
                    }
                }
            } else {
                String left = num.substring(0, half);
                String right = num.substring(half, num.length());
                if(reverseString(left).compareTo(right) >= 0) {
                    result = left + reverseString(left);
                } else {
                    int prev_length = left.length();
                    BigInteger bnum = new BigInteger(left);
                    bnum = bnum.add(BigInteger.ONE);
                    left = bnum.toString();
                    if(prev_length < left.length()) {
                        result = left.substring(0, left.length() - 1) + reverseString(left);
                    } else {
                        result = left + reverseString(left);
                    }
                }
            }
            return result;
        }
    }
    private static String reverseString(String num) {
        StringBuilder result = new StringBuilder();
        for(int i = num.length(); i > 0; i--) {
            result.append(num.substring(i - 1, i));
        }
        return result.toString();
    }
}
