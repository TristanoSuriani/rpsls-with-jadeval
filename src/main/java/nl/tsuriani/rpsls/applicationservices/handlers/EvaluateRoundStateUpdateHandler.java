package nl.tsuriani.rpsls.applicationservices.handlers;

import nl.suriani.jadeval.execution.decision.DecisionsDelegate;
import nl.tsuriani.rpsls.applicationservices.context.RoundContext;
import nl.tsuriani.rpsls.applicationservices.RoundResult;
import nl.tsuriani.rpsls.applicationservices.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.SystemEvent;

import java.util.Optional;

public class EvaluateRoundStateUpdateHandler extends RPSLSStateUpdateHandler {
	private DecisionsDelegate<RoundContext> roundRules;

	public EvaluateRoundStateUpdateHandler(DecisionsDelegate<RoundContext> roundRules) {
		super(Session.Status.bothPlayersHaveChosen);
		this.roundRules = roundRules;
	}

	@Override
	public void enterState(SessionContext sessionContext) {
		var movePlayer1 = sessionContext.getSession().getMovePlayer1();
		var movePlayer2 = sessionContext.getSession().getMovePlayer2();
		var roundContext = RoundContext.builder()
				.movePlayer1(movePlayer1)
				.movePlayer2(movePlayer2)
				.build();

		getRoundResult(roundContext).ifPresent(
				result -> {
					switch (result) {
						case PLAYER1_WINS:
							sessionContext.getSession().getPlayer1().incrementScore();
							sessionContext.setPlayer1Score(sessionContext.getSession().getPlayer1().getScore());
							sessionContext.setPlayer2Score(sessionContext.getSession().getPlayer2().getScore());
							sessionContext.setSystemEvent(SystemEvent.PLAYER1_SCORES);
							break;

						case PLAYER2_WINS:
							sessionContext.getSession().getPlayer2().incrementScore();
							sessionContext.setPlayer1Score(sessionContext.getSession().getPlayer1().getScore());
							sessionContext.setPlayer2Score(sessionContext.getSession().getPlayer2().getScore());
							sessionContext.setSystemEvent(SystemEvent.PLAYER2_SCORES);
							break;

						case DRAW:
							sessionContext.setPlayer1Score(sessionContext.getSession().getPlayer1().getScore());
							sessionContext.setPlayer2Score(sessionContext.getSession().getPlayer2().getScore());
							sessionContext.setSystemEvent(SystemEvent.NOBODY_SCORES);
					}
				});
	}

	private Optional<RoundResult> getRoundResult(RoundContext roundContext) {
		return roundRules.apply(roundContext).getResponses().stream()
				.map(RoundResult::valueOf)
				.findFirst();
	}
}
