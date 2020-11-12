package nl.tsuriani.rpsls.infra.service;

import nl.suriani.jadeval.execution.decision.DecisionResults;
import nl.tsuriani.rpsls.domain.PlayerIdentity;
import nl.tsuriani.rpsls.domain.SessionIdentity;
import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.domain.round.PostRoundResult;
import nl.tsuriani.rpsls.domain.round.Round;
import nl.tsuriani.rpsls.domain.round.RoundContext;
import nl.tsuriani.rpsls.domain.round.RoundEvent;
import nl.tsuriani.rpsls.domain.round.RoundState;
import nl.tsuriani.rpsls.domain.session.Player;
import nl.tsuriani.rpsls.domain.session.Session;
import nl.tsuriani.rpsls.domain.session.SessionContext;
import nl.tsuriani.rpsls.domain.session.SessionState;
import nl.tsuriani.rpsls.infra.db.SessionsRepositoryImpl;
import nl.tsuriani.rpsls.infra.di.PostRoundDecisions;
import nl.tsuriani.rpsls.infra.di.RoundRules;
import nl.tsuriani.rpsls.infra.di.RoundValidations;
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

	@Inject
	RoundValidations roundValidations;

	@Inject
	RoundRules roundRules;

	@Inject
	PostRoundDecisions postRoundDecisions;

	@Override
	public List<Session> findAll() {
		return sessionsRepository.findAll();
	}

	@Override
	public Session joinOrCreateSession(String clientUUID, String username) {
		var session = sessionsRepository.findSessionWaitingForPlayer2()
				.map(s -> s.player2(Player.builder()
						.identity(PlayerIdentity.builder()
								.clientUUID(UUID.fromString(clientUUID))
								.username(username)
								.build())
						.build())
						.state(getNextState(s.state(), SessionContext.UserAction.JOIN)))
				.orElse(Session.builder()
						.identity(SessionIdentity.builder()
								.sessionUUID(UUID.randomUUID())
								.build())
						.player1(Player.builder()
								.identity(PlayerIdentity.builder()
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
	public void cancelSession(String sessionUUID) {
		sessionsRepository.findBySessionIdentity(SessionIdentity.builder()
				.sessionUUID(UUID.fromString(sessionUUID))
				.build())
				.map(session -> session.state(getNextState(session.state(), SessionContext.UserAction.DISCONNECT)))
				.ifPresent(session -> sessionsRepository.saveSession(session));
	}

	@Override
	public void chooseMove(String sessionUUID, String clientUUID, String username, Move move) {
		sessionsRepository.findBySessionIdentity(SessionIdentity.builder()
				.sessionUUID(UUID.fromString(sessionUUID))
				.build())
				.ifPresent(session -> chooseMove(session, determineRoundEventForChooseMove(session, clientUUID, username), move));
	}

	private RoundEvent determineRoundEventForChooseMove(Session session, String clientUUID, String username) {
		if (session.player1().isSamePlayer(clientUUID, username)) {
			return RoundEvent.PLAYER1_CHOOSES;
		} else {
			return session.getPlayer2()
					.filter(player -> player.isSamePlayer(clientUUID, username))
					.map(player -> RoundEvent.PLAYER2_CHOOSES)
					.orElse(null);
		}
	}

	private void chooseMove(Session session, RoundEvent roundEvent, Move move) {
		var sessionWithMove = addMoveToSession(session, roundEvent, move);

		var currentContext = getRoundContextFromSession(sessionWithMove)
				.roundEvent(roundEvent);

		if (currentContext.state().equals(RoundState.bothPlayersHaveChosen)) {
			sessionsRepository.saveSession(
					evaluateRoundAndAdvanceGame(
							sessionWithMove,
							roundEvent,
							getRoundContextFromSession(sessionWithMove)
									.roundEvent(roundEvent)));
		} else {
			sessionsRepository.saveSession(sessionWithMove);
		}
	}

	private Session evaluateRoundAndAdvanceGame(Session session, RoundEvent roundEvent, RoundContext roundContext) {
		roundValidations.getValidationsDelegate().apply(roundContext);
		var resultRoundRules = roundRules.getDecisionsDelegate().apply(roundContext);
		var sessionWithIncrementedScore = getSessionWithIncrementedScore(session, resultRoundRules);
		var roundContextWithIncrementedScore = getRoundContextFromSession(sessionWithIncrementedScore)
				.roundEvent(getRoundEventFromDecisionsResult(resultRoundRules));
		var resultPostRoundDecisions = postRoundDecisions.getDecisionsDelegate().apply(roundContextWithIncrementedScore);
		var postRoundResult = getPostRoundResultFromDecisionsResult(resultPostRoundDecisions);
		return startNewRoundOrTerminateSession(sessionWithIncrementedScore, postRoundResult);
	}

	private Session getSessionWithIncrementedScore(Session session, DecisionResults resultRoundRules) {
		if (resultRoundRules.getResponses().contains(RoundEvent.PLAYER1_SCORES.name())) {
			return session.player1(session.player1().incrementScore());
		} else if (resultRoundRules.getResponses().contains(RoundEvent.PLAYER2_SCORES.name())) {
			return session.player2(session.player2().incrementScore());
		}
		return session;
	}

	private Session addMoveToSession(Session session, RoundEvent roundEvent, Move move) {
		if (session.getRound().isEmpty()) {
			session = session.round(Round.builder()
					.state(RoundState.nobodyHasChosen)
					.build());
		}
		var sessionWithMove = session.round(Round.builder()
				.player1Move(roundEvent == RoundEvent.PLAYER1_CHOOSES ? move : session.round().player1Move())
				.player2Move(roundEvent == RoundEvent.PLAYER2_CHOOSES ? move : session.round().player2Move())
				.state(session.round().state())
				.build());

		var nextState = getNextState(sessionWithMove, roundEvent);
		return sessionWithMove.round(Round.builder()
				.player1Move(sessionWithMove.round().player1Move())
				.player2Move(sessionWithMove.round().player2Move())
				.state(nextState)
				.build());
	}

	private Session startNewRoundOrTerminateSession(Session session, PostRoundResult postRoundResult) {
		switch (postRoundResult) {
			case PLAYER1_WINS:
				return session.state(sessionWorkflow.getWorkflowDelegate().apply(SessionContext.builder()
						.gameEvent(SessionContext.GameEvent.PLAYER1_WINS)
						.sessionState(session.state())
						.build())
						.getSessionState());

			case PLAYER2_WINS:
				return session.state(sessionWorkflow.getWorkflowDelegate().apply(SessionContext.builder()
						.gameEvent(SessionContext.GameEvent.PLAYER2_WINS)
						.sessionState(session.state())
						.build())
						.getSessionState());

			case NEW_ROUND:
				return session.round(null);

			default:
				return session;
		}
	}

	private SessionState getNextState(SessionState currentState, SessionContext.UserAction userAction) {
		return getNextState(currentState, userAction, null);
	}

	private SessionState getNextState(SessionState currentState, SessionContext.GameEvent gameEvent) {
		return getNextState(currentState, null, gameEvent);
	}

	private SessionState getNextState(SessionState currentState, SessionContext.UserAction userAction, SessionContext.GameEvent gameEvent) {
		return sessionWorkflow.getWorkflowDelegate().apply(SessionContext.builder()
				.userAction(userAction)
				.gameEvent(gameEvent)
				.sessionState(currentState)
				.build()).getSessionState();
	}

	private RoundState getNextState(Session session, RoundEvent roundEvent) {
		return roundWorkflow.getWorkflowDelegate()
				.apply(getRoundContextFromSession(session)
						.roundEvent(roundEvent))
				.state();
	}

	private RoundContext getRoundContextFromSession(Session session) {
		return RoundContext.builder()
				.player1Move(session.round().player1Move())
				.player2Move(session.round().player2Move())
				.player1Score(session.player1().getScore())
				.player2Score(session.getPlayer2()
						.map(Player::getScore)
						.orElse(0))
				.state(session.round().state())
				.build();
	}

	private RoundEvent getRoundEventFromDecisionsResult(DecisionResults decisionResults) {
		if (decisionResults.getResponses().contains(RoundEvent.PLAYER1_SCORES.name())) {
			return RoundEvent.PLAYER1_SCORES;
		} else if (decisionResults.getResponses().contains(RoundEvent.PLAYER2_SCORES.name())) {
			return RoundEvent.PLAYER2_SCORES;
		} else if (decisionResults.getResponses().contains(RoundEvent.NOBODY_SCORES.name())) {
			return RoundEvent.NOBODY_SCORES;
		}
		return null;
	}

	private PostRoundResult getPostRoundResultFromDecisionsResult(DecisionResults decisionResults) {
		if (decisionResults.getResponses().contains(PostRoundResult.PLAYER1_WINS.name())) {
			return PostRoundResult.PLAYER1_WINS;
		} else if (decisionResults.getResponses().contains(PostRoundResult.PLAYER2_WINS.name())) {
			return PostRoundResult.PLAYER2_WINS;
		} else if (decisionResults.getResponses().contains(PostRoundResult.NEW_ROUND.name())) {
			return PostRoundResult.NEW_ROUND;
		}
		return null;
	}
}
