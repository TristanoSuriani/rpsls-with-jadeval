package nl.tsuriani.rpsls.infra.service;

import nl.tsuriani.rpsls.domain.PlayerIdentity;
import nl.tsuriani.rpsls.domain.SessionIdentity;
import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.domain.session.Player;
import nl.tsuriani.rpsls.domain.session.Session;
import nl.tsuriani.rpsls.domain.session.SessionState;
import nl.tsuriani.rpsls.infra.db.SessionsRepositoryImpl;
import nl.tsuriani.rpsls.infra.di.RoundWorkflow;
import nl.tsuriani.rpsls.infra.di.SessionWorkflow;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RPSLSSessionService implements SessionService {
	@Inject
	SessionsRepositoryImpl sessionsRepository;

	@Inject
	SessionWorkflow sessionWorkflow;

	@Inject
	RoundWorkflow roundWorkflow;

	@Override
	public List<Session> findAll() {
		return sessionsRepository.findAll();
	}

	@Override
	public Session joinOrCreateSession(String clientUUID, String username) {
		var session = sessionsRepository.findSessionWaitingForPlayer2()
				.map(s -> s.player2(Player.builder()
						.playerIdentity(PlayerIdentity.builder()
								.clientUUID(UUID.fromString(clientUUID))
								.username(username)
								.build())
						.build())
						.state(SessionState.player2HasJoined))
				.orElse(Session.builder()
						.identity(SessionIdentity.builder()
								.sessionUUID(UUID.randomUUID())
								.build())
						.player1(Player.builder()
								.playerIdentity(PlayerIdentity.builder()
										.clientUUID(UUID.fromString(clientUUID))
										.username(username)
										.build())
								.build())
						.state(SessionState.player1HasJoined)
						.build());

		sessionsRepository.saveSession(session);
		return session;
	}

	@Override
	public void cancelSession(String uuid, String playerUUID, String username) {

	}

	@Override
	public void chooseMove(String uuid, String playerUUID, String username, Move move) {

	}

	@Override
	public void deleteAll() {

	}
}
