import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tools {
    public static ArrayList<String> rooms = new ArrayList<>();
    public static Map<String, String> userInGame = new HashMap<>();
    public static Map<String, Room> roomMap = new HashMap<>();

    public static RequestMessage getRequest(String xml) {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[] {RequestMessage.class});
        xStream.alias("xml", RequestMessage.class);
        xStream.aliasField("ToUserName", RequestMessage.class, "ToUserName");
        xStream.aliasField("FromUserName", RequestMessage.class, "FromUserName");
        xStream.aliasField("CreateTime", RequestMessage.class, "CreateTime");
        xStream.aliasField("MsgType", RequestMessage.class, "MsgType");
        xStream.aliasField("Content", RequestMessage.class, "Content");
        xStream.aliasField("MsgId", RequestMessage.class, "MsgId");
        return (RequestMessage)xStream.fromXML(xml);
    }

    public static String ReplyMessage(RequestMessage message) {
        String xml = "<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%d</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[%s]]></Content></xml>";
        return String.format(xml, message.getFromUserName(), message.getToUserName(), new Date().getTime(), con(message));
    }
    public static void close(String roomId) {
        rooms.remove(roomId);
        for (String key : userInGame.keySet()) {
            String room = userInGame.get(key);
            if (room.equals(roomId)) {
                userInGame.remove(key);
            }
        }
        roomMap.remove(roomId);
    }

    private static String con(RequestMessage message) {
        String reply;
        String contentU = message.getContent();
        switch (contentU) {
            case "c":
                if (userInGame.get(message.getFromUserName()) != null){
                    reply = "you are already in game";
                }else {
                    Room room = new Room();
                    String roomId = room.create(message.getFromUserName());
                    roomMap.put(roomId, room);
                    reply = "房号为: " + roomId + "\n接下来请输入玩家数(9-16)";
                }
                break;
            default:
                if (userInGame.get(message.getFromUserName()) != null){
                    String id = userInGame.get(message.getFromUserName());
                    reply = roomMap.get(id).parse(message);
                }else {
                    if (roomMap.get(message.getContent()) != null){
                        reply = roomMap.get(message.getContent()).parse(message);
                    }else {
                        reply = "创建房间的话请回复\"c\"";
                    }
                }
        }
        return reply;
    }
}
