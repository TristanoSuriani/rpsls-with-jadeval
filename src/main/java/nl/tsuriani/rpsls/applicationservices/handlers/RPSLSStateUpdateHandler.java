package nl.tsuriani.rpsls.applicationservices.handlers;

import nl.suriani.jadeval.execution.shared.BaseStateUpdateEventHandler;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;

public abstract class RPSLSStateUpdateHandler extends BaseStateUpdateEventHandler<SessionContext> {

	public RPSLSStateUpdateHandler(Session.Status status) {
		super(status.name());
	}

	@Override
	public void enterState(SessionContext sessionContext) {
		super.enterState(sessionContext);
	}

	@Override
	public void exitState(SessionContext sessionContext) {
		super.exitState(sessionContext);
	}
}
