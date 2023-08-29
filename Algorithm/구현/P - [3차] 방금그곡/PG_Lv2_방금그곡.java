import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PG_Lv2_방금그곡 {
    static List<String> titleIdx = new ArrayList<>();
    static class Music implements Comparable<Music>{
        int playTime;

        String title;
        String note;

        public Music(int playTime, String title, String note){
            this.playTime = playTime;
            this.title = title;
            this.note = note;
        }

        @Override
        public int compareTo(Music o) {
            if (this.playTime == o.playTime) {
                return titleIdx.indexOf(this.title) - titleIdx.indexOf(o.title);
            }
            return  o.playTime - this.playTime;
        }
    }
    public static String solution(String m, String[] musicinfos){
        List<Music> musics = new ArrayList<>();
        m = m.replace("A#", "1");
        m = m.replace("C#", "3");
        m = m.replace("D#", "4");
        m = m.replace("F#", "6");
        m = m.replace("G#", "7");
        for(String musicinfo : musicinfos){
            String[] data = musicinfo.split(",");
            String startTime = data[0];
            String endTime = data[1];
            String musicTitle = data[2];
            titleIdx.add(musicTitle);
            String musicNote = data[3];
            musicNote = musicNote.replace("A#", "1");
            musicNote = musicNote.replace("C#", "3");
            musicNote = musicNote.replace("D#", "4");
            musicNote = musicNote.replace("F#", "6");
            musicNote = musicNote.replace("G#", "7");
            int playTime = getPlayTime(startTime, endTime);
            musics.add(new Music(playTime, musicTitle, getMusicNote(playTime, musicNote)));
        }
        Collections.sort(musics);
        for (Music music : musics) {
            if (music.note.contains(m)) {
                return music.title;
            }
        }
        return "(None)";
    }

    private static int getPlayTime(String startTime, String endTime){
        String[] startTimeData = startTime.split(":");
        String[] endTimeData = endTime.split(":");
        int startHour = Integer.parseInt(startTimeData[0]);
        int startMinute = Integer.parseInt(startTimeData[1]);
        int endHour = Integer.parseInt(endTimeData[0]);
        int endMinute = Integer.parseInt(endTimeData[1]);
        return (endHour - startHour) * 60 + (endMinute - startMinute);
    }

    private static String getMusicNote(int playTime, String musicNote){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playTime; i++) {
            sb.append(musicNote.charAt(i % musicNote.length()));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(solution("ABCDEFG", new String[] {"12:00,12:14,HELLO,CDEFGAB", "13:00,13:05,WORLD,ABCDEF"}));
        System.out.println(solution("CC#BCC#BCC#BCC#B", new String[] {"03:00,03:30,FOO,CC#B", "04:00,04:08,BAR,CC#BCC#BCC#B"}));
    }
}
