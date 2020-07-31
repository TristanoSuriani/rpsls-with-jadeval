package nl.tsuriani.rpsls.domainservices.mutation;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.RPSLS;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domainservices.rule.Rules;

import java.util.UUID;

@AllArgsConstructor
public class MutateSession {
	private Rules rules;

	public RPSLS createSessionWithPlayer1(String uuid, String name) {
		return new RPSLS(UUID.randomUUID(),
				new Session.Player1(UUID.fromString(uuid), name),
				null,
				null,
				null,
				Session.Status.WAITING_FOR_PLAYER2);
	}

	public RPSLS addPlayer2ToSession(RPSLS session, String uuid, String name) {
		return startNewRound(new RPSLS(session.getUuid(),
				session.getPlayer1(),
				new Session.Player2(UUID.fromString(uuid), name),
				null,
				null,
				Session.Status.PLAYER2_JOINED));
	}

	public RPSLS addMovePlayer1(RPSLS session, Session.MovePlayer1 movePlayer1) {
		switch (session.getStatus()) {

			case WAITING_FOR_PLAYER1_TO_MOVE:
				return new RPSLS(session.getUuid(),
					session.getPlayer1(),
					session.getPlayer2(),
					movePlayer1,
					session.getMovePlayer2(),
					Session.Status.READY_FOR_ROUND_EVALUATION);

			case WAITING_FOR_BOTH_PLAYERS_TO_MOVE:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						movePlayer1,
						session.getMovePlayer2(),
					Session.Status.WAITING_FOR_PLAYER2_TO_MOVE);

			default:
				return session;
		}
	}

	public RPSLS addMovePlayer2(RPSLS session, Session.MovePlayer2 movePlayer2) {
		switch (session.getStatus()) {

			case WAITING_FOR_PLAYER2_TO_MOVE:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						session.getMovePlayer1(),
						movePlayer2,
						Session.Status.READY_FOR_ROUND_EVALUATION);

			case WAITING_FOR_BOTH_PLAYERS_TO_MOVE:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						session.getMovePlayer1(),
						movePlayer2,
						Session.Status.WAITING_FOR_PLAYER1_TO_MOVE);

			default:
				return session;
		}
	}

	public RPSLS evaluateRound(RPSLS session) {
		switch (session.getStatus()) {
			case READY_FOR_ROUND_EVALUATION:
				final RPSLS evaluationResult;
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

	public RPSLS player1Won(RPSLS session) {
		switch (session.getStatus()) {
			case ROUND_EVALUATED:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.PLAYER1_WON);

			default:
				return session;
		}
	}

	public RPSLS player2Won(RPSLS session) {
		switch (session.getStatus()) {
			case ROUND_EVALUATED:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.PLAYER2_WON);

			default:
				return session;
		}
	}

	public RPSLS player1CancelsTheSession(RPSLS session) {
		switch (session.getStatus()) {
			case PLAYER1_WON:
			case PLAYER2_WON:
			case CANCELLED_BY_PLAYER1:
			case CANCELLED_BY_PLAYER2:
			case CANCELLED_BY_SYSTEM:
				return session;

			default:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.CANCELLED_BY_PLAYER1);
		}
	}

	public RPSLS player2CancelsTheSession(RPSLS session) {
		switch (session.getStatus()) {
			case WAITING_FOR_PLAYER2:
			case PLAYER1_WON:
			case PLAYER2_WON:
			case CANCELLED_BY_PLAYER1:
			case CANCELLED_BY_PLAYER2:
			case CANCELLED_BY_SYSTEM:
				return session;

			default:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.CANCELLED_BY_PLAYER2);
		}
	}

	public RPSLS systemCancelsTheSession(RPSLS session) {
		switch (session.getStatus()) {
			case PLAYER1_WON:
			case PLAYER2_WON:
			case CANCELLED_BY_PLAYER1:
			case CANCELLED_BY_PLAYER2:
			case CANCELLED_BY_SYSTEM:
				return session;

			default:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null,
						Session.Status.CANCELLED_BY_SYSTEM);
		}
	}

	private RPSLS increaseScorePlayer1(RPSLS session) {
		return new RPSLS(session.getUuid(),
				new Session.Player1(session.getPlayer1().getUuid(),
						session.getPlayer1().getName(),
						session.getPlayer1().getScore() + 1),
				session.getPlayer2(),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				Session.Status.ROUND_EVALUATED);
	}

	private RPSLS increaseScorePlayer2(RPSLS session) {
		return new RPSLS(session.getUuid(),
				session.getPlayer1(),
				new Session.Player2(session.getPlayer2().getUuid(),
						session.getPlayer2().getName(),
						session.getPlayer2().getScore() + 1),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				Session.Status.ROUND_EVALUATED);
	}

	private RPSLS leaveScoreUnchanged(RPSLS session) {
		return new RPSLS(session.getUuid(),
				session.getPlayer1(),
				session.getPlayer2(),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				Session.Status.ROUND_EVALUATED);
	}

	private RPSLS startNewRound(RPSLS session) {
		switch (session.getStatus()) {
			case PLAYER2_JOINED:
			case ROUND_EVALUATED:
				return new RPSLS(session.getUuid(),
						session.getPlayer1(),
						session.getPlayer2(),
						null,
						null, Session.Status.WAITING_FOR_BOTH_PLAYERS_TO_MOVE);

			default:
				return session;
		}
	}

	private boolean player1ReachedTheMaximumScore(RPSLS session) {
		return session.getPlayer1().getScore() == rules.getMaximumScore();
	}

	private boolean player2ReachedTheMaximumScore(RPSLS session) {
		return session.getPlayer2().getScore() == rules.getMaximumScore();
	}
}
