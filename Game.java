import java.util.*;

import occupations.*;

public class Game {

    private ArrayList<baseOccupation> occQ;

    private Map<String, baseOccupation> players;

    private Map<String, Integer> box;

    private String status;

    private int num_w;

    private int num_v;

    private String roomId;

    private int peopleToJoin;

    private boolean voteOk;

    public String getStatus() {
        return status;
    }

    public Map<String, baseOccupation> getPlayers() {
        return players;
    }

    Game(String a) {
        roomId = a;
        status = "no";
        occQ = new ArrayList<>();
        players = new HashMap<>();
        box = new HashMap<>();
        num_v = 0;
        num_w = 0;
        peopleToJoin = 0;
        voteOk = false;
    }

    private String getMostVote() {
        checkVote();
        if (!voteOk) return null;
        int max = 0;
        String who = "";
        for (String key :
                box.keySet()) {
            if (box.get(key) >= max) {
                max = box.get(key);
                who = key;
            }
        }
        players.get(who).setAlive(false);
        box.clear();
        return who + " :" + max;
    }

    private String vote(String from, String who) {
        String info;
        if (status.equals("ne")) {
            String ocu = players.get(from).getOccupatin();
            switch (ocu) {
                case "seer":
                    info = who + "号是" + players.get(who).getOccupatin();
                    break;
                    default:
                        info = "不得行";
            }
        }else {
            if (box.get(who) != null) {
                box.put(who, box.get(who) + 1);
            } else {
                box.put(who, 1);
            }
            players.get(from).setAction(true);
            info = "你投了" + who + "一票";
        }
        return info;
    }

    private String checkVote() {
        StringBuilder builder = new StringBuilder("投票情况");
        int alive = 0;
        int action = 0;
        for (String key : players.keySet()) {
            builder.append(key);
            if (players.get(key).isAlive()) {
                alive += 1;
                if (players.get(key).isAction()) {
                    action += 1;
                    builder.append("已经投票");
                }else {
                    builder.append("未投票");
                }
                builder.append("\n");
            } else {
                builder.append("已经死亡\n");
            }
        }
        if (alive == action) voteOk = true;
        return builder.toString();
    }

    private void distributionOccupation(int w, int v, int s){
        for (int i = 0; i < w; i++) {
            occQ.add(new werewolves());
        }
        for (int i = 0; i < v; i++) {
            occQ.add(new villager());
        }
        for (int i = 0; i < s; i++) {
            occQ.add(new special(i));
        }
        peopleToJoin = w + v + s;
        Collections.shuffle(occQ);
    }

    private void close() {
        Tools.close(roomId);
    }

    public String getV(){
        String info;
        int i = 0;
        StringBuilder builder = new StringBuilder();
        for (String key : players.keySet()) {
            if (players.get(key).getOccupatin().equals("werewolves")) {
                i += 1;
                builder.append(key);
                builder.append(", ");
            }
        }
        if (i > 1) {
            builder.append("和你自己是狼");
            info = builder.toString();
        }else {
            info = "只有你自己是狼";
        }
        return info;
    }

    public String start(String n) {
        switch (n){
            case "9":
                distributionOccupation(3, 3, 3);
                break;
            case "10":
                distributionOccupation(3, 4, 3);
                break;
            case "11":
                distributionOccupation(3, 5, 3);
                break;
            case "12":
                distributionOccupation(4, 4, 4);
                break;
            case "13":
                distributionOccupation(4, 5, 4);
                break;
            case "14":
                distributionOccupation(4, 6, 4);
                break;
            case "15":
                distributionOccupation(5, 5, 5);
                break;
            case "16":
                distributionOccupation(5, 6, 5);
                break;
                default:
                    return "限9-16人";
        }
        status = "toJoin";
        return "ok! 等待玩家加入\n回复\"how\"来查看房主指令";
    }

    private String getP() {
        if (players.size() == 0) {
            return "还没人加入房间, 快把房号告诉小伙伴";
        }else {
            StringBuilder buffer = new StringBuilder("人员配置:\n");
            for (String key : players.keySet()) {
                buffer.append(key);
                buffer.append(": ");
                buffer.append(players.get(key).getOccupatin());
                buffer.append("(");
                buffer.append(players.get(key).isAlive() ? "Alive" : "Die");
                buffer.append(")");
                buffer.append("\n");
            }
            if (players.size() == peopleToJoin) buffer.append("全员到齐");
            return buffer.toString();
        }
    }


    private String forceOpen() {
        if (status.equals("toJoin")) {
            if (isWin() == null) {
                occQ.clear();
                status = "ne";
                return getP();
            }else {
                return isWin();
            }
        }else {
            return "游戏已经开始";
        }
    }

    private String next() {
        String info;
        switch (status) {
            case "ns":
                status = "na";
                info = "请狼人投票";
                break;
            case "na":
                if (isWin() != null) return isWin();
                status = "ne";
                info = getMostVote() != null ? getMostVote() : checkVote();
                break;
            case "ne":
                status = "ds";
                info = "发言环节";
                break;
            case "ds":
                status = "de";
                info = "放逐公投";
                break;
            case "de":
                if (isWin() != null) return isWin();
                status = "ns";
                info = getMostVote();
                break;
                default:
                    info = "错误指令";
                    break;
        }
        return info;
    }

    public int join() {
        int i = occQ.size();
        if (i > 0) {
            if (i == 1) status = "ne";
            baseOccupation toPut = occQ.remove(i - 1);
            players.put(i + "", toPut);
            if (toPut.getOccupatin().equals("werewolves")) {
                num_w += 1;
            } else if (toPut.getOccupatin().equals("villager")) {
                num_v += 1;
            }
            return i;
        }else {
            return -2;
        }
    }

    private boolean canAction(String who){
        String ocu = players.get(who).getOccupatin();
        boolean isok = false;
        switch (status) {
            case "na":
                if (ocu.equals("werewolves")) isok = true;
                break;
            case "ne":
                if (ocu.equals("seer")) isok = true;
                break;
            case "de":
                isok = true;
                break;
                default:
                    break;
        }
        return isok;
    }

    public String setStatus(String con){
        String info ;
        switch (con) {
            case "now":
                info = status;
                break;
            case "check":
                info = checkVote();
                break;
            case "next":
                info = next();
                break;
            case "gp":
                info = getP();
                break;
            case "st":
                info = forceOpen();
                break;
            case "how":
                info = "next: 开启下一个阶段\nnow: 查看当前阶段\ncheck: 查看投票情况\n" +
                        "gp: 获取当前玩家\nst: 强制开启游戏\nhow: 查看指令\nend: 结束游戏";
                break;
            case "end":
                info = "游戏结束";
                close();
                break;
                default:
                    if (status.equals("no")) info = start(con); else {
                        info = "请输入正确指令";
                    }
        }
        return info;
    }

    private String isWin() {
        if (num_w == 0 && num_v == 0) {
            close();
            return "空房间, 游戏结束";
        }
        if (num_v == 0) {
            close();
            return "狼人胜利";
        }
        if (num_w == 0) {
            close();
            return "村民胜利";
        }
        return null;
    }

    public String parse(String who, String con){
        if (!players.get(who).isAlive()) return "你已经死了";
        if (canAction(who)) {
            return vote(who, con);
        }else {
            return "还不到你行动";
        }
    }
}
