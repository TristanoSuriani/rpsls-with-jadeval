package nl.tsuriani.rpsls.applicationservices.context;

import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.UserEvent;

import java.util.UUID;

public class SessionContextFactory {
	public SessionContext makePlayer1JoinsSessionContext(String playerUUID, String username) {
		return SessionContext.builder()
				.session(Session.builder()
						.uuid(UUID.randomUUID())
						.player1(new Session.Player1(UUID.fromString(playerUUID), username))
						.build())
				.status(Session.Status.waitingForPlayer1ToJoin)
				.userEvent(UserEvent.PLAYER1_JOINS)
				.build();
	}

	public SessionContext makePlayer2JoinsSessionContext(Session session, String playerUUID, String username) {
		return SessionContext.builder()
				.session(Session.builder()
						.uuid(session.getUuid())
						.player1(session.getPlayer1())
						.player2(new Session.Player2(UUID.fromString(playerUUID), username))
						.build())
				.status(session.getStatus())
				.userEvent(UserEvent.PLAYER2_JOINS)
				.build();
	}

	public SessionContext makePlayer1ChoosesSessionContext(Session session, Session.Move move) {
		return SessionContext.builder()
				.session(Session.builder()
						.uuid(session.getUuid())
						.player1(session.getPlayer1())
						.player2(session.getPlayer2())
						.movePlayer1(move)
						.movePlayer2(session.getMovePlayer2())
						.build())
				.status(session.getStatus())
				.userEvent(UserEvent.PLAYER1_CHOOSES)
				.player1Score(session.getPlayer1().getScore())
				.player2Score(session.getPlayer2().getScore())
				.build();
	}

	public SessionContext makePlayer2ChoosesSessionContext(Session session, Session.Move move) {
		return SessionContext.builder()
				.session(Session.builder()
						.uuid(session.getUuid())
						.player1(session.getPlayer1())
						.player2(session.getPlayer2())
						.movePlayer1(session.getMovePlayer1())
						.movePlayer2(move)
						.build())
				.status(session.getStatus())
				.userEvent(UserEvent.PLAYER2_CHOOSES)
				.player1Score(session.getPlayer1().getScore())
				.player2Score(session.getPlayer2().getScore())
				.build();
	}

	public SessionContext makePlayer1DisconnectsSessionContext(Session session) {
		return SessionContext.builder()
				.session(session)
				.status(session.getStatus())
				.userEvent(UserEvent.PLAYER1_DISCONNECTS)
				.player1Score(session.getPlayer1().getScore())
				.player2Score(session.getPlayer2().getScore())
				.build();
	}

	public SessionContext makePlayer2DisconnectsSessionContext(Session session) {
		return SessionContext.builder()
				.session(session)
				.status(session.getStatus())
				.userEvent(UserEvent.PLAYER2_DISCONNECTS)
				.player1Score(session.getPlayer1().getScore())
				.player2Score(session.getPlayer2().getScore())
				.build();
	}

	public SessionContext makeSessionContextWithoutInput(Session session) {
		return SessionContext.builder()
				.session(session)
				.status(session.getStatus())
				.build();
	}
}
