package nl.tsuriani.rpsls.infra.web.socket;

import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.domain.session.Session;
import nl.tsuriani.rpsls.infra.web.controller.SessionFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket/rpsls/{clientUUID}/{username}",
        decoders = {WebSocketInMessageDecoder.class},
        encoders = {WebSocketOutMessageEncoder.class}
)
@ApplicationScoped
public class RPSLSSocket {

    @Inject
    SessionFacade sessionFacade;

    @Inject
    ConnectionsContainer connectionsContainer;

    @OnOpen
    public void onOpen(Session session, @PathParam("clientUUID") String clientUUID,
                       @PathParam("username") String username) {

        sessionFacade.registerPlayer(clientUUID, username);
    }

    @OnClose
    public void onClose(@PathParam("clientUUID") String clientUUID,
                        @PathParam("username") String username) {

        connectionsContainer.iFeelLucky(clientUUID, username)
                .ifPresent(connectionKey -> {
                    sessionFacade.disconnect(connectionKey.getSessionUUID());
                    connectionsContainer.remove(connectionKey);
                    connectionsContainer.findOpponent(connectionKey)
                        .ifPresent(opponentConnectionKey -> {
                            // notify opponent about the disconnection
                        });
                });
    }


    @OnError
    public void onError(Session session, @PathParam("clientUUID") String clientUUID,
                        @PathParam("username") String username, Throwable throwable) {

        /// nothing yet
    }

    @OnMessage
    public void onMessage(WebSocketInMessage message) {
        sessionFacade.chooseMove(message.sessionUUID, message.payload.clientUUID, message.payload.name, Move.valueOf(message.payload.move));
    }
}
