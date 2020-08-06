package nl.tsuriani.rpsls.applicationservices;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.mutation.MutateSession;
import nl.tsuriani.rpsls.domainservices.SessionService;
import nl.tsuriani.rpsls.infra.db.SessionEntity;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class RPSLSSessionService implements SessionService {
	private MutateSession mutateSession;

	@Override
	public List<Session> findAll() {
		return SessionEntity.findAll().list().stream()
				.map(baseEntity -> (SessionEntity) baseEntity)
				.map(SessionEntity::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public void joinOrCreateSession(String playerUUID, String username) {
		SessionEntity sessionEntity = SessionEntity.findOpenSession()
				.orElseGet(() -> new SessionEntity(mutateSession.createSessionWithPlayer1(playerUUID, username)));

		if (sessionEntity.getPlayer1().getUuid().equals(playerUUID) && sessionEntity.getPlayer1().getName().equals(username)) {
			SessionEntity.persist(sessionEntity);
		} else {
			Session session = SessionEntity.fromEntity(sessionEntity);
			sessionEntity.merge(mutateSession.addPlayer2ToSession(session, playerUUID, username));
			SessionEntity.update(sessionEntity);
		}
	}

	@Override
	public void cancelSession(String uuid, String playerUUID, String username) {
		SessionEntity.findByUUID(uuid)
				.ifPresent((session) -> cancelSession(session, playerUUID, username));

	}

	private void cancelSession(SessionEntity session, String playerUUID, String username) {
		session.merge(mutateSession.cancelSession(SessionEntity.fromEntity(session), playerUUID, username));
		SessionEntity.update(session);
	}

	@Override
	public void chooseMove(String uuid, String playerUUID, String username, Session.Move move) {
		SessionEntity.findByUUID(uuid)
				.ifPresent(session -> chooseMove(session, playerUUID, username, move));
	}

	private void chooseMove(SessionEntity session, String playerUUID, String username, Session.Move move) {
		session.merge(mutateSession.addMove(SessionEntity.fromEntity(session), playerUUID, username, move));
		session.merge(mutateSession.evaluateRound(SessionEntity.fromEntity(session)));
		SessionEntity.update(session);
	}

	@Override
	public void deleteAll() {
		SessionEntity.deleteAll();
	}
}
