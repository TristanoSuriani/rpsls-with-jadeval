package nl.tsuriani.rpsls.infra.service;

import lombok.AllArgsConstructor;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.tsuriani.rpsls.infra.service.context.SessionContext;
import nl.tsuriani.rpsls.infra.service.context.SessionContextFactory;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.infra.db.AuditLogEntity;
import nl.tsuriani.rpsls.infra.db.SessionEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
	public SessionContext joinOrCreateSession(String playerUUID, String username) {
		Optional<SessionEntity> sessionEntity = SessionEntity.findOpenSession();
		if (sessionEntity.isPresent()) {
			Session session = SessionEntity.toSession(sessionEntity.get());
			var sessionContext = sessionContextFactory.makePlayer2JoinsSessionContext(session, playerUUID, username);
			updateSessionState(sessionEntity.get(), sessionContext, 1);
			return sessionContext;
		} else {
			var sessionContext = sessionContextFactory.makePlayer1JoinsSessionContext(playerUUID, username);
			persistSessionState(sessionContext);
			return sessionContext;
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

	private SessionContext persistSessionState(SessionContext sessionContext) {
		var currentContext = sessionContext;
		var sessionEntity = new SessionEntity(sessionContext);
		sessionContext = workflow.apply(sessionContext);
		sessionEntity.merge(sessionContext);
		sessionEntity.persist();
		return currentContext;
	}

	private SessionContext updateSessionState(SessionEntity sessionEntity, SessionContext sessionContext) {
		var currentContext = workflow.apply(sessionContext);
		sessionEntity.merge(sessionContext);
		sessionEntity.update();
		return currentContext;
	}

	private SessionContext updateSessionState(SessionEntity sessionEntity, SessionContext sessionContext, int times) {
		return Stream.iterate(0, (i) -> i + 1)
				.limit(Math.max(times, 0))
				.map(i -> sessionContext)
				.reduce(sessionContext,
						(previous, next) -> updateSessionState(sessionEntity, previous));
	}
}
