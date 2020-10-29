package nl.tsuriani.rpsls.applicationservices;

import lombok.AllArgsConstructor;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.applicationservices.context.SessionContextFactory;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domainservices.SessionService;
import nl.tsuriani.rpsls.infra.db.AuditLogEntity;
import nl.tsuriani.rpsls.infra.db.SessionEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
public class RPSLSSessionService implements SessionService {
	private WorkflowDelegate<SessionContext> workflow;
	private SessionContextFactory sessionContextFactory;

	@Override
	public List<Session> findAll() {
		return SessionEntity.findAll().list().stream()
				.map(baseEntity -> (SessionEntity) baseEntity)
				.map(SessionEntity::toSession)
				.collect(Collectors.toList());
	}

	@Override
	public void joinOrCreateSession(String playerUUID, String username) {
		Optional<SessionEntity> sessionEntity = SessionEntity.findOpenSession();
		if (sessionEntity.isPresent()) {
			Session session = SessionEntity.toSession(sessionEntity.get());
			var sessionContext = sessionContextFactory.makePlayer2JoinsSessionContext(session, playerUUID, username);
			updateSessionState(sessionEntity.get(), sessionContext, 1);
		} else {
			var sessionContext = sessionContextFactory.makePlayer1JoinsSessionContext(playerUUID, username);
			persistSessionState(sessionContext);
		}
	}

	@Override
	public void cancelSession(String uuid, String playerUUID, String username) {
		SessionEntity.findByUUID(uuid)
				.ifPresent((session) -> cancelSession(session, playerUUID, username));

	}

	private void cancelSession(SessionEntity sessionEntity, String playerUUID, String username) {
		if (isPlayer1(sessionEntity, playerUUID, username)) {
			var sessionContext = sessionContextFactory.makePlayer1DisconnectsSessionContext(SessionEntity.toSession(sessionEntity));
			updateSessionState(sessionEntity, sessionContext);
		} else if (isPlayer2(sessionEntity, playerUUID, username)) {
			var sessionContext = sessionContextFactory.makePlayer2DisconnectsSessionContext(SessionEntity.toSession(sessionEntity));
			updateSessionState(sessionEntity, sessionContext);
		}
	}

	private boolean isPlayer1(SessionEntity sessionEntity, String playerUUID, String username) {
		SessionEntity.PlayerEntity player1 = sessionEntity.getPlayer1();
		return playerUUID.equals(player1.getUuid()) && username.equals(player1.getName());
	}

	private boolean isPlayer2(SessionEntity sessionEntity, String playerUUID, String username) {
		SessionEntity.PlayerEntity player2 = sessionEntity.getPlayer2();
		return player2 != null && playerUUID.equals(player2.getUuid()) && username.equals(player2.getName());
	}

	@Override
	public void chooseMove(String uuid, String playerUUID, String username, Session.Move move) {
		SessionEntity.findByUUID(uuid)
				.ifPresent(session -> chooseMove(session, playerUUID, username, move));
	}

	private void chooseMove(SessionEntity sessionEntity, String playerUUID, String username, Session.Move move) {
		if (isPlayer1(sessionEntity, playerUUID, username)) {
			var sessionContext = sessionContextFactory.makePlayer1ChoosesSessionContext(SessionEntity.toSession(sessionEntity), move);
			updateSessionState(sessionEntity, sessionContext, 3);
		} else if (isPlayer2(sessionEntity, playerUUID, username)) {
			var sessionContext = sessionContextFactory.makePlayer2ChoosesSessionContext(SessionEntity.toSession(sessionEntity), move);
			updateSessionState(sessionEntity, sessionContext, 3);
		}
	}

	@Override
	public void deleteAll() {
		SessionEntity.deleteAll();
		AuditLogEntity.deleteAll();
	}

	private void persistSessionState(SessionContext sessionContext) {
		var sessionEntity = new SessionEntity(sessionContext);
		workflow.apply(sessionContext);
		sessionEntity.merge(sessionContext);
		sessionEntity.persist();
		sessionContext.setUserEvent(null);
		sessionContext.setSystemEvent(null);
	}

	private void updateSessionState(SessionEntity sessionEntity, SessionContext sessionContext) {
		workflow.apply(sessionContext);
		sessionEntity.merge(sessionContext);
		sessionEntity.update();
	}

	private void updateSessionState(SessionEntity sessionEntity, SessionContext sessionContext, int times) {
		times = Math.max(times, 0);
		for (int i = 0; i < times; i++) {
			updateSessionState(sessionEntity, sessionContext);
		}
		sessionContext.setUserEvent(null);
		sessionContext.setSystemEvent(null);
	}
}
