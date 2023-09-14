import java.util.*;

public class PG_Lv2_과제_진행하기 {
    public String[] solution(String[][] plans) {
        Assignment[] arr = new Assignment[plans.length];
        for (int i = 0; i < plans.length; i++) {
            Assignment ass = new Assignment(plans[i][0], plans[i][1], plans[i][2]);
            arr[i] = ass;
        }

        Arrays.sort(arr, (o1, o2) -> {
            return o1.start - o2.start;
        });


        Stack<Assignment> stack = new Stack<>();  // 진행 중인 과제
        List<String> ans = new ArrayList<>();

        int curTime = -1;                         // 현재 시간 초기화

        for (int i = 0; i < arr.length; i++) {
            /* 진행 중인 과제가 없는 경우 */
            if (stack.isEmpty()) {
                stack.push(arr[i]);
                continue;
            }

            /* 진행 중인 과제와 새로운 과제가 있는 경우 */
            Assignment curAss = stack.peek();   // 진행중 과제
            Assignment newAss = arr[i];         // 새로운 과제

            // 새로운 과제의 시작시간과 진행중 과제의 종료 시간 비교
            curTime = curAss.start;

            // 현재 과제 시작 시간 + 작업 시간이 새로운 과제 시작 시간 보다 같거나 작은 경우
            if (curTime + curAss.time <= newAss.start) {
                recursivePop(stack, newAss, curTime, ans);
            } else {
                // 현재 작업 중단하고 작업한 시간 갱신 및 새 작업 시작
                curAss.time -= newAss.start - curTime;
            }
            stack.push(newAss);
        }

        /* 새로운 과제가 없는 경우 */
        while (!stack.isEmpty()) {
            ans.add(stack.pop().name);
        }
        return ans.toArray(new String[0]);
    }

    public void recursivePop(Stack<Assignment> stack, Assignment newAss, int curTime, List<String> ans) {
        if (stack.isEmpty()) {
            return;
        }
        Assignment curAss = stack.peek();   // 진행중 과제
        if (curTime + curAss.time <= newAss.start) {
            ans.add(stack.pop().name);
            recursivePop(stack, newAss, curTime + curAss.time, ans);
        } else {
            curAss.time -= newAss.start - curTime;
        }
    }



    static class Assignment {
        private String name;
        private int start;
        private int time;

        public Assignment(String name, String start, String time) {
            this.name = name;
            this.start = timeToMinute(start);
            this.time = Integer.parseInt(time);
        }

        public int timeToMinute(String start) {
            String[] arr = start.split(":");
            int h = Integer.parseInt(arr[0]) * 60;
            int m = Integer.parseInt(arr[1]);
            return h + m;
        }
    }
}
