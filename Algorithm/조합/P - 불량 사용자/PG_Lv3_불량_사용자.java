import java.util.*;

public class PG_Lv3_불량_사용자 {
    Set<List<String>> ids = new HashSet<>();

    public int solution(String[] user_id, String[] banner_id) {
        boolean[] visited = new boolean[user_id.length];
        dfs(user_id, banner_id, visited, 0, 0);
        return ids.size();
    }

    private void dfs(String[] user_id, String[] banner_id, boolean[] visited, int start, int count) {
        if (count == banner_id.length) {
            List<String> list = new ArrayList<>();
            for(int i = 0; i < visited.length; i++) {
                if(visited[i]) {
                    list.add(user_id[i]);
                }
            }
            ids.add(list);
            return;
        }

        for(int i = start; i < banner_id.length; i++) {
            String banned = banner_id[i];
            for (int j = 0; j < user_id.length; j++) {
                String user = user_id[j];
                boolean flag = true;
                if(banned.length() != user.length()) {
                    flag = false;
                }else{
                    for(int k = 0; k < banned.length(); k++) {
                        if(banned.charAt(k) == '*') {
                            continue;
                        }
                        if(banned.charAt(k) != user.charAt(k)){
                            flag = false;
                            break;
                        }
                    }
                }

                if(flag && !visited[j]) {
                    visited[j] = true;
                    dfs(user_id, banner_id, visited, i+1, count+1);
                    visited[j] = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        PG_Lv3_불량_사용자 p = new PG_Lv3_불량_사용자();
        p.solution(new String[]{"frodo", "fradi", "crodo", "abc123", "frodoc"}, new String[]{"fr*d*", "*rodo", "******", "******"});
    }
}


