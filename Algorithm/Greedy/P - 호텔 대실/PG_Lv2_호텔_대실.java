import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class PG_Lv2_호텔_대실 {
    static class Room implements Comparable<Room>{
        int start;
        int end;

        public Room(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Room o) {
            return this.end - o.end;
        }
    }
    public static int solution(String[][] book_time){
        Queue<Room> pq = new PriorityQueue<>();
        Arrays.sort(book_time, Comparator.comparing(timeRange -> timeRange[0]));
        for (String[] roomTime : book_time) {
            int startTime = switchMinute(roomTime[0]);
            int endTime = switchMinute(roomTime[1]);
            Room now = new Room(startTime, endTime);
            if (pq.isEmpty()) {
                pq.add(now);
                continue;
            }
            if (pq.peek().end + 10 <= startTime) {
                pq.poll();
                pq.add(now);
            }else{
                pq.add(now);
            }
        }
        return pq.size();
    }

    private static int switchMinute(String time){
        String[] hourAndMinute = time.split(":");
        int hour = Integer.parseInt(hourAndMinute[0]);
        int minute = Integer.parseInt(hourAndMinute[1]);
        return hour*60 + minute;
    }


    public static void main(String[] args) {
        System.out.println(solution(new String[][] {{"15:00", "17:00"}, {"16:40", "18:20"}, {"14:20", "15:20"}, {"14:10", "19:20"}, {"18:20", "21:20"}}));
        System.out.println(solution(new String[][] {{"09:10", "10:10"}, {"10:20", "12:20"}}));
        System.out.println(solution(new String[][] {{"10:20", "12:30"}, {"10:20", "12:30"}, {"10:20", "12:30"}}));
    }
}
