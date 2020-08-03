package nl.tsuriani.rpsls.applicationservices;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.mutation.MutateSession;
import nl.tsuriani.rpsls.domainservices.SessionRepository;
import nl.tsuriani.rpsls.domainservices.SessionService;

import java.util.Optional;

@AllArgsConstructor
public class RPSLSSessionService implements SessionService {
	private SessionRepository sessionRepository;
	private MutateSession mutateSession;

	@Override
	public Session joinOrCreateSession(String playerUUID, String username) {
		Optional<Session> openSession = sessionRepository.findOpenSession();
		return openSession.map(session -> mutateSession.addPlayer2ToSession(session, playerUUID, username))
				.orElseGet(() -> mutateSession.createSessionWithPlayer1(playerUUID, username));
	}

	@Override
	public void cancelSession(String uuid, String playerUUID, String username) {
		sessionRepository.findByUUID(uuid)
				.map(session -> mutateSession.cancelSession(session, playerUUID, username))
				.ifPresent(sessionRepository::save);
	}

	@Override
	public void chooseMove(String uuid, String playerUUID, String username, Session.Move move) {
		sessionRepository.findByUUID(uuid)
				.map(session -> mutateSession.addMove(session, playerUUID, username, move))
				.map(mutateSession::evaluateRound)
				.ifPresent(sessionRepository::save);

	}
}
