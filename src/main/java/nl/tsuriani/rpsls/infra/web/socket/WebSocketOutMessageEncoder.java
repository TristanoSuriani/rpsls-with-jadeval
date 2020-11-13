package nl.tsuriani.rpsls.infra.web.socket;

import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketOutMessageEncoder implements Encoder.Text<WebSocketOutMessage> {
    private static Gson gson = new Gson();

    @Override
    public String encode(WebSocketOutMessage webSocketOutMessage) throws EncodeException {
        return gson.toJson(webSocketOutMessage);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // no op
    }

    @Override
    public void destroy() {
        // no op
    }
}
