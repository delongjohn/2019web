public class ReplyMessage {
    private String ToUserName;
    private String FromUserName;
    private long CreateTime;
    private String MessageType;
    private String Content;
    private String FuncFlag;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getFuncFlag() {
        return FuncFlag;
    }

    public void setFuncFlag(String funcFlag) {
        FuncFlag = funcFlag;
    }
}
