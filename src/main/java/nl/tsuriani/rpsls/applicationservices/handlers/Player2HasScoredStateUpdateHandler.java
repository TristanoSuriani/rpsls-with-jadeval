package nl.tsuriani.rpsls.applicationservices.handlers;

import lombok.extern.java.Log;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.SystemEvent;

@Log
public class Player2HasScoredStateUpdateHandler extends RPSLSStateUpdateHandler {
	public Player2HasScoredStateUpdateHandler() {
		super(Session.Status.player2HasScored);
	}

	@Override
	public void enterState(SessionContext sessionContext) {
		log.info("player 2 has scored!");
	}

	@Override
	public void exitState(SessionContext sessionContext) {
		sessionContext.setSystemEvent(SystemEvent.PLAYER2_SCORES);
	}
}
