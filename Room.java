import java.util.HashMap;
import java.util.Map;

public class Room {
    private Map<String, String> usersToNum;
    private String holder;
    public String id;
    private Game game;

    Room(){
        usersToNum = new HashMap<>();
    }

    public String create(String holder){
        while (Tools.rooms.contains(id = (int)(Math.random() * 900 + 100) + "")){
            id = (int)(Math.random() * 900 +100) + "";
        }
        Tools.userInGame.put(holder, id);
        this.holder = holder;
        game = new Game(id);
        return id;
    }

    public String parse(RequestMessage message){
        if (!game.getStatus().equals("toJoin")){
            if (message.getFromUserName().equals(holder)){
                return game.setStatus(message.getContent());
            }else {
                return game.getStatus().equals("no") ? "等待房主设置" : game.parse(usersToNum.get(message.getFromUserName()), message.getContent());
            }
        }else {
            if (message.getFromUserName().equals(holder)) {//法官部分
                return game.setStatus(message.getContent());
            }else {//普通玩家部分
                if (usersToNum.get(message.getFromUserName()) != null){
                    return "已经加入房间";
                }else {
                    int num = game.join();
                    String otherInfo = "";
                    switch (num) {
                        case 1:
                            usersToNum.put(message.getFromUserName(), num + "");
                            otherInfo += "请法官开始游戏\n";
                            break;
                        case -2:
                            return "人满为患";
                        default:
                            usersToNum.put(message.getFromUserName(), num + "");
                            otherInfo += "ok\n";
                    }
                    Tools.userInGame.put(message.getFromUserName(), id);
                    String Id = usersToNum.get(message.getFromUserName());
                    if (game.getPlayers().get(Id).getOccupatin().equals("werewolves")) {
                        otherInfo += game.getV();
                    }
                    return Id + ": " + game.getPlayers().get(Id).getOccupatin() + "\n" + otherInfo;
                }
            }
        }
    }
}
