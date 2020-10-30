package nl.tsuriani.rpsls.applicationservices.handlers;

import nl.suriani.jadeval.execution.decision.DecisionsDelegate;
import nl.tsuriani.rpsls.applicationservices.context.RoundContext;
import nl.tsuriani.rpsls.applicationservices.RoundResult;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.SystemEvent;

import java.util.Optional;

public class EvaluateRoundOnStateUpdateTransformer extends RPSLSOnStateUpdateTransformer {
	private DecisionsDelegate<RoundContext> roundRules;

	public EvaluateRoundOnStateUpdateTransformer(DecisionsDelegate<RoundContext> roundRules) {
		super(Session.Status.bothPlayersHaveChosen);
		this.roundRules = roundRules;
	}

	@Override
	public SessionContext enterState(SessionContext sessionContext) {
		var movePlayer1 = sessionContext.getSession().getMovePlayer1();
		var movePlayer2 = sessionContext.getSession().getMovePlayer2();
		var roundContext = RoundContext.builder().movePlayer1(movePlayer1).movePlayer2(movePlayer2).build();

		var roundResult = getRoundResult(roundContext);
		if (roundResult.isPresent()) {
			var result = roundResult.get();
			switch (result) {
				case PLAYER1_WINS:
					return SessionContext.builder()
							.session(Session.builder()
									.uuid(sessionContext.getSession().getUuid())
									.player1(new Session.Player1(sessionContext.getSession().getPlayer1().getUuid(),
											sessionContext.getSession().getPlayer1().getName(),
											sessionContext.getSession().getPlayer1().getScore() + 1))
									.player2(sessionContext.getSession().getPlayer2())
									.movePlayer1(movePlayer1)
									.movePlayer2(movePlayer2)
									.status(sessionContext.getSession().getStatus()).build())
							.player1Score(sessionContext.getSession().getPlayer1().getScore() + 1)
							.player2Score(sessionContext.getSession().getPlayer2().getScore())
							.systemEvent(SystemEvent.PLAYER1_SCORES)
							.status(sessionContext.getStatus())
							.build();

				case PLAYER2_WINS:
					return SessionContext.builder()
							.session(Session.builder()
									.uuid(sessionContext.getSession().getUuid())
									.player1(sessionContext.getSession().getPlayer1())
									.player2(new Session.Player2(sessionContext.getSession().getPlayer2().getUuid(),
											sessionContext.getSession().getPlayer2().getName(),
											sessionContext.getSession().getPlayer2().getScore() + 1))
									.movePlayer1(movePlayer1)
									.movePlayer2(movePlayer2)
									.status(sessionContext.getSession().getStatus())
									.build())
							.player1Score(sessionContext.getSession().getPlayer1().getScore())
							.player2Score(sessionContext.getSession().getPlayer2().getScore() + 1)
							.systemEvent(SystemEvent.PLAYER2_SCORES)
							.status(sessionContext.getStatus())
							.build();

				case DRAW:
					return SessionContext.builder()
							.session(sessionContext.getSession())
							.systemEvent(SystemEvent.NOBODY_SCORES)
							.player1Score(sessionContext.getPlayer1Score())
							.player2Score(sessionContext.getPlayer2Score())
							.status(sessionContext.getStatus())
							.build();
			}
		}
		return sessionContext;
	}

	private Optional<RoundResult> getRoundResult(RoundContext roundContext) {
		return roundRules.apply(roundContext).getResponses().stream()
				.map(RoundResult::valueOf)
				.findFirst();
	}
}
