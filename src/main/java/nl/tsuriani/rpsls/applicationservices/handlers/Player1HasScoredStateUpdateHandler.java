package nl.tsuriani.rpsls.applicationservices.handlers;

import lombok.extern.java.Log;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.SystemEvent;

@Log
public class Player1HasScoredStateUpdateHandler extends RPSLSStateUpdateHandler {
	public Player1HasScoredStateUpdateHandler() {
		super(Session.Status.player1HasScored);
	}

	@Override
	public void enterState(SessionContext sessionContext) {
		log.info("player 1 has scored!");
	}

	@Override
	public void exitState(SessionContext sessionContext) {
		sessionContext.setSystemEvent(SystemEvent.PLAYER1_SCORES);
	}
}
