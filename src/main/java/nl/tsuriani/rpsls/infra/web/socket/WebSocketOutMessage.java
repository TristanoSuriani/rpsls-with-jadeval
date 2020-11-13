package nl.tsuriani.rpsls.infra.web.socket;

public class WebSocketOutMessage {
    public Header header;
    public Score payload;
    public String sessionUUID;

    public enum Header {
        NEW_ROUND, SESSION_TERMINATED, SESSION_CANCELLED
    }

    public class Score {
        public PlayerWithMove player1;
        public PlayerWithMove player2;
    }

    public class PlayerWithMove {
        public String name;
        public String clientUUID;
        public Integer score;
        public String move;
    }
}
