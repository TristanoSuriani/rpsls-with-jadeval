package nl.tsuriani.rpsls.infra.service.interceptors;

import lombok.extern.java.Log;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;
import nl.tsuriani.rpsls.infra.service.context.SessionContext;
import nl.tsuriani.rpsls.infra.db.AuditLogEntity;

import java.time.LocalDateTime;

@Log
public class AuditTransitionAttempted implements TransitionAttemptedEventHandler<SessionContext> {

	@Override
	public void handle(SessionContext context) {
		var systemEvent = context.getSystemEvent() == null ? null : context.getSystemEvent().name();
		var userEvent = context.getUserEvent() == null ? null : context.getUserEvent().name();
		var movePlayer1 = context.getSession().getMovePlayer1() == null ? null : context.getSession().getMovePlayer1().name();
		var movePlayer2 = context.getSession().getMovePlayer2() == null ? null : context.getSession().getMovePlayer2().name();
		var uuid = context.getSession().getUuid() == null ? null : context.getSession().getUuid().toString();
		AuditLogEntity.persist(AuditLogEntity.builder()
				.status(context.getStatus().toString())
				.movePlayer1(movePlayer1)
				.movePlayer2(movePlayer2)
				.scorePlayer1(context.getPlayer1Score())
				.scorePlayer2(context.getPlayer2Score())
				.systemEvent(systemEvent)
				.userEvent(userEvent)
				.sessionUUID(uuid)
				.dateLogged(LocalDateTime.now())
				.build());
	}
}
