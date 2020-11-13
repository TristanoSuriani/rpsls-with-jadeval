package nl.tsuriani.rpsls.infra.web.socket;

import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WebSocketInMessageDecoder implements Decoder.Text<WebSocketInMessage> {
    private static Gson gson = new Gson();

    @Override
    public WebSocketInMessage decode(String json) throws DecodeException {
        return gson.fromJson(json, WebSocketInMessage.class);
    }

    @Override
    public boolean willDecode(String json) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        //no op
    }

    @Override
    public void destroy() {
        //no op
    }
}
