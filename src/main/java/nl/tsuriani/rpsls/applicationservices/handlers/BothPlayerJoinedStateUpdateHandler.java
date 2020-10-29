package nl.tsuriani.rpsls.applicationservices.handlers;

import lombok.extern.java.Log;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;

@Log
public class BothPlayerJoinedStateUpdateHandler extends RPSLSStateUpdateHandler {
	public BothPlayerJoinedStateUpdateHandler() {
		super(Session.Status.bothPlayersJoined);
	}

	@Override
	public void exitState(SessionContext sessionContext) {
		// TODO send message on websocket
		log.info("Both players have joined!");
	}
}
