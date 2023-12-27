import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BJ_10825_국영수 {
    static class Student{
        String name;
        int koreanScore;
        int englishScore;
        int mathScore;

        public Student(String name, int koreanScore, int englishScore, int mathScore) {
            this.name = name;
            this.koreanScore = koreanScore;
            this.englishScore = englishScore;
            this.mathScore = mathScore;
        }
    }

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String name = st.nextToken();
            int koreanScore = Integer.parseInt(st.nextToken());
            int englishScore = Integer.parseInt(st.nextToken());
            int mathScore = Integer.parseInt(st.nextToken());
            students.add(new Student(name, koreanScore, englishScore, mathScore));
        }
        students.sort((o1, o2) -> {
            if (o1.koreanScore == o2.koreanScore && o1.englishScore == o2.englishScore && o1.mathScore == o2.mathScore) {
                return o1.name.compareTo(o2.name);
            } else if (o1.koreanScore == o2.koreanScore && o1.englishScore == o2.englishScore) {
                return Integer.compare(o2.mathScore, o1.mathScore);
            } else if (o1.koreanScore == o2.koreanScore) {
                return Integer.compare(o1.englishScore, o2.englishScore);
            }
            return Integer.compare(o2.koreanScore, o1.koreanScore);
        });
        for (Student student : students) {
            System.out.println(student.name);
        }
    }
}
