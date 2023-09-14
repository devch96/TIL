public class PG_Lv2_두_원_사이의_정수_쌍 {
    public static long solution(int r1, int r2){
        long answer = (long)(r2-r1+1)*4L;
        int smallY = 0;
        int bigY = 0;
        for(int x=1;x<r2;x++){
            smallY = (int)Math.sqrt((long)r1*r1-(long)x*x);
            bigY = (int)Math.sqrt((long)r2*r2-(long)x*x);
            answer += (long)(bigY-smallY)*4;
            if(x<r1 && Math.sqrt((long)r1*r1-(long)x*x)%1 ==0) answer+=4L;
        }
        return answer;
    }
}
