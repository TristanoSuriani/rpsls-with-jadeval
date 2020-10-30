package nl.tsuriani.rpsls.infra.service.interceptors;

import nl.suriani.jadeval.execution.shared.BaseOnStateUpdateContextTransformer;
import nl.tsuriani.rpsls.infra.service.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;

public abstract class RPSLSOnStateUpdateTransformer extends BaseOnStateUpdateContextTransformer<SessionContext> {

	public RPSLSOnStateUpdateTransformer(Session.Status status) {
		super(status.name());
	}

	@Override
	public SessionContext enterState(SessionContext sessionContext) {
		return super.enterState(sessionContext);
	}

	@Override
	public SessionContext exitState(SessionContext sessionContext) {
		return super.exitState(sessionContext);
	}
}
