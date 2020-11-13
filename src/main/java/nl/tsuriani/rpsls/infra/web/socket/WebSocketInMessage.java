package nl.tsuriani.rpsls.infra.web.socket;

public class WebSocketInMessage {
    public Header header;
    public Choice payload;
    public String sessionUUID;

    public enum Header {
        CHOOSE
    }

    public class Choice {
        public String clientUUID;
        public String name;
        public String move;
    }
}
