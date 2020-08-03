package nl.tsuriani.rpsls.domain.mutation;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.rule.Rules;

import java.util.UUID;

@AllArgsConstructor
public class MutateSession {
	private Rules rules;

	public Session createSessionWithPlayer1(String uuid, String name) {
		return new Session(UUID.randomUUID(),
				new Session.Player1(UUID.fromString(uuid), name),
				null,
				null,
				null,
				Session.Status.WAITING_FOR_PLAYER2);
	}

	public Session addPlayer2ToSession(Session session, String uuid, String name) {
		return startNewRound(new Session(session.getUuid(),
				session.getPlayer1(),
				new Session.Player2(UUID.fromString(uuid), name),
				null,
				null,
				Session.Status.PLAYER2_JOINED));
	}

	public Session addMove(Session session, String playerUUID, String username, Session.Move move) {
		if (isPlayer1(session, playerUUID, username)) {
			return addMovePlayer1(session, new Session.MovePlayer1(move.getName()));
		} else if (isPlayer2(session, playerUUID, username)) {
			return addMovePlayer2(session, new Session.MovePlayer2(move.getName()));
		} else {
			return session;
		}
	}

	public Session evaluateRound(Session session) {
		switch (session.getStatus()) {
			case READY_FOR_ROUND_EVALUATION:
				final Session evaluationResult;
				if (rules.firstMoveScoresAgainstTheSecondOne(session.getMovePlayer1(), session.getMovePlayer2())) {
					evaluationResult = increaseScorePlayer1(session);
					if (player1ReachedTheMaximumScore(evaluationResult)) {
						return player1Won(evaluationResult);
					} else {
						return startNewRound(evaluationResult);
					}
				} else if (rules.secondMoveScoresAgainstTheFirstOne(session.getMovePlayer1(), session.getMovePlayer2())) {
					evaluationResult = increaseScorePlayer2(session);
					if (player2ReachedTheMaximumScore(evaluationResult)) {
						return player2Won(evaluationResult);
					} else {
						return startNewRound(evaluationResult);
					}
				} else {
					evaluationResult = leaveScoreUnchanged(session);
					return startNewRound(evaluationResult);
				}

			default:
				return session;
		}
	}

	public Session player1Won(Session session) {
		switch (session.getStatus()) {
			case ROUND_EVALUATED:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.PLAYER1_WON);

			default:
				return session;
		}
	}

	public Session player2Won(Session session) {
		switch (session.getStatus()) {
			case ROUND_EVALUATED:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.PLAYER2_WON);

			default:
				return session;
		}
	}

	public Session cancelSession(Session session, String playerUUID, String username) {
		if (isPlayer1(session, playerUUID, username)) {
			return player1CancelsTheSession(session);
		} else if (isPlayer2(session, playerUUID, username)) {
			return player2CancelsTheSession(session);
		}
		else return session;
	}

	Session addMovePlayer1(Session session, Session.MovePlayer1 movePlayer1) {
		switch (session.getStatus()) {

			case WAITING_FOR_PLAYER1_TO_MOVE:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						movePlayer1,
						session.getMovePlayer2(),
						Session.Status.READY_FOR_ROUND_EVALUATION);

			case WAITING_FOR_BOTH_PLAYERS_TO_MOVE:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						movePlayer1,
						session.getMovePlayer2(),
						Session.Status.WAITING_FOR_PLAYER2_TO_MOVE);

			default:
				return session;
		}
	}

	Session addMovePlayer2(Session session, Session.MovePlayer2 movePlayer2) {
		switch (session.getStatus()) {

			case WAITING_FOR_PLAYER2_TO_MOVE:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						session.getMovePlayer1(),
						movePlayer2,
						Session.Status.READY_FOR_ROUND_EVALUATION);

			case WAITING_FOR_BOTH_PLAYERS_TO_MOVE:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						session.getMovePlayer1(),
						movePlayer2,
						Session.Status.WAITING_FOR_PLAYER1_TO_MOVE);

			default:
				return session;
		}
	}

	Session player1CancelsTheSession(Session session) {
		switch (session.getStatus()) {
			case PLAYER1_WON:
			case PLAYER2_WON:
			case CANCELLED_BY_PLAYER1:
			case CANCELLED_BY_PLAYER2:
			case CANCELLED_BY_SYSTEM:
				return session;

			default:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.CANCELLED_BY_PLAYER1);
		}
	}

	Session player2CancelsTheSession(Session session) {
		switch (session.getStatus()) {
			case WAITING_FOR_PLAYER2:
			case PLAYER1_WON:
			case PLAYER2_WON:
			case CANCELLED_BY_PLAYER1:
			case CANCELLED_BY_PLAYER2:
			case CANCELLED_BY_SYSTEM:
				return session;

			default:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.CANCELLED_BY_PLAYER2);
		}
	}

	public Session systemCancelsTheSession(Session session) {
		switch (session.getStatus()) {
			case PLAYER1_WON:
			case PLAYER2_WON:
			case CANCELLED_BY_PLAYER1:
			case CANCELLED_BY_PLAYER2:
			case CANCELLED_BY_SYSTEM:
				return session;

			default:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.CANCELLED_BY_SYSTEM);
		}
	}

	public Session keepSessionUnchanged(Session session) {
		return session;
	}

	private Session increaseScorePlayer1(Session session) {
		return new Session(session.getUuid(),
				new Session.Player1(session.getPlayer1().getUuid(),
						session.getPlayer1().getName(),
						session.getPlayer1().getScore() + 1),
				session.getPlayer2(),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				Session.Status.ROUND_EVALUATED);
	}

	private Session increaseScorePlayer2(Session session) {
		return new Session(session.getUuid(),
				session.getPlayer1(),
				new Session.Player2(session.getPlayer2().getUuid(),
						session.getPlayer2().getName(),
						session.getPlayer2().getScore() + 1),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				Session.Status.ROUND_EVALUATED);
	}

	private Session leaveScoreUnchanged(Session session) {
		return new Session(session.getUuid(),
				session.getPlayer1(),
				session.getPlayer2(),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				Session.Status.ROUND_EVALUATED);
	}

	private Session startNewRound(Session session) {
		switch (session.getStatus()) {
			case PLAYER2_JOINED:
			case ROUND_EVALUATED:
				return new Session(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null, Session.Status.WAITING_FOR_BOTH_PLAYERS_TO_MOVE);

			default:
				return session;
		}
	}

	private boolean isPlayer1(Session session, String playerUUID, String username) {
		return session.getPlayer1().getUuid().toString().equals(playerUUID) && session.getPlayer1().getName().equals(username);
	}

	private boolean isPlayer2(Session session, String playerUUID, String username) {
		return session.getPlayer2().getUuid().toString().equals(playerUUID) && session.getPlayer2().getName().equals(username);
	}

	private boolean player1ReachedTheMaximumScore(Session session) {
		return session.getPlayer1().getScore() == rules.getMaximumScore();
	}

	private boolean player2ReachedTheMaximumScore(Session session) {
		return session.getPlayer2().getScore() == rules.getMaximumScore();
	}
}
