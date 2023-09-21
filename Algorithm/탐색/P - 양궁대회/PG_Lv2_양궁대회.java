import java.util.*;

public class PG_Lv2_양궁대회 {

    static List<int[]> ryanCombinations;
    static int[] apeachScoreArr;

    public static int[] solution(int n, int[] info){
        apeachScoreArr = info;
        ryanCombinations = new ArrayList<>();
        dfs(n,10,new int[12]);
        Collections.sort(ryanCombinations, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                for (int i = o1.length - 1; i >= 0; i--) {
                    if (o1[i] < o2[i]) {
                        return 1;
                    } else if (o1[i] > o2[i]) {
                        return -1;
                    }
                }
                return 0;
            }
        });
       if(ryanCombinations.get(0)[11] <= 0){
           return new int[] {-1};
       }

        return Arrays.copyOfRange(ryanCombinations.get(0), 0, 11);
    }

    private static void dfs(int remainArrow, int shootScore, int[] ryanScoreArr){
        if(remainArrow == 0){
            ryanScoreArr[11] = getScoreDiff(ryanScoreArr);
            ryanCombinations.add(ryanScoreArr);
            return;
        }
        if(shootScore < 0){
            return;
        }
        int apeachScore = apeachScoreArr[10-shootScore];
        int[] clone = ryanScoreArr.clone();
        int nowShoot = apeachScore+1;
        clone[10-shootScore] = 0;
        dfs(remainArrow, shootScore-1, clone);
        if(remainArrow-nowShoot < 0){
            clone[10-shootScore] = remainArrow;
            dfs(0,shootScore-1,clone);
        }else{
            clone[10-shootScore] = nowShoot;
            dfs(remainArrow - nowShoot, shootScore-1, clone);
        }
    }

    private static int getScoreDiff(int[] ryanScoreArr){
        int score = 0;
        for (int i = 0; i < 11; i++) {
            if (ryanScoreArr[i] > apeachScoreArr[i]) {
                score+= 10-i;
            } else if (ryanScoreArr[i] == 0 && apeachScoreArr[i] == 0) {
                continue;
            }else{
                score -= (10 - i);
            }
        }
        return score;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(10, new int[] {0,0,0,0,0,0,0,0,3,4,3})));
    }
}
