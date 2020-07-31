package nl.tsuriani.rpsls.domainservices.mutation;

import nl.tsuriani.rpsls.domain.RPSLS;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.Session.Status;
import nl.tsuriani.rpsls.domainservices.rule.RPSLSRulesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static nl.tsuriani.rpsls.domain.Session.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class MutateSessionTest {

	private MutateSession mutateSession;
	private Session.Player1 player1;
	private Session.Player2 player2;

	@BeforeEach
	void setUp() {
		mutateSession = new MutateSession(new RPSLSRulesBuilder().build());
		player1 = new Session.Player1(UUID.randomUUID(), "Jack");
		player2 = new Session.Player2(UUID.randomUUID(), "Jill");
	}

	@Test
	void createSessionWithPlayer1() {
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		assertEquals(player1.getUuid(), session.getPlayer1().getUuid());
		assertEquals(player1.getName(), session.getPlayer1().getName());
		assertEquals(0, session.getPlayer1().getScore());
		assertNull(session.getPlayer2());
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(WAITING_FOR_PLAYER2, session.getStatus());
	}

	@Test
	void addPlayer2ToSession() {
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		session = mutateSession.addPlayer2ToSession(session, player2.getUuid().toString(), player2.getName());
		assertEquals(player1.getUuid(), session.getPlayer1().getUuid());
		assertEquals(player1.getName(), session.getPlayer1().getName());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(player2.getUuid(), session.getPlayer2().getUuid());
		assertEquals(player2.getName(), session.getPlayer2().getName());
		assertEquals(0, session.getPlayer2().getScore());
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(WAITING_FOR_BOTH_PLAYERS_TO_MOVE, session.getStatus());
	}

	@Test
	void player1Wins() {
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		session = mutateSession.addPlayer2ToSession(session, player2.getUuid().toString(), player2.getName());

		// Test 1. Player 1 adds a move. Expected: the move is registered, the status is changed accordingly.
		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.rock().getName()));

		assertNotNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_PLAYER2_TO_MOVE, session.getStatus());

		// Test 2. Trying to evaluate the round before Player 2 adds a move. Expected: no change in status and in result.
		session = mutateSession.evaluateRound(session);

		assertNotNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_PLAYER2_TO_MOVE, session.getStatus());

		// Test 3. Player 2 adds a move. Expected: the move is registered, the status is changed accordingly.
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.rock().getName()));

		assertNotNull(session.getMovePlayer1());
		assertNotNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(READY_FOR_ROUND_EVALUATION, session.getStatus());

		// Test 3. The round is evaluated. Expected: since it is a draw, the score is unchanged;
		// the status is changed accordingly  and the moves are unregistered.
		session = mutateSession.evaluateRound(session);
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_BOTH_PLAYERS_TO_MOVE, session.getStatus());

		// Test 4. This time Player 1 scores. Expected: score is updated;
		// the status is changed accordingly  and the moves are unregistered.
		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.rock().getName()));
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.scissors().getName()));
		session = mutateSession.evaluateRound(session);
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(1, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_BOTH_PLAYERS_TO_MOVE, session.getStatus());

		// Test 5. Player 1 scores other two times and reaches the maximum score. Expected:
		// Player 1 wins. The moves are unregistered and the status is changed accordingly.
		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.rock().getName()));
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.scissors().getName()));
		session = mutateSession.evaluateRound(session);

		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.rock().getName()));
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.scissors().getName()));
		session = mutateSession.evaluateRound(session);

		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(3, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(PLAYER1_WON, session.getStatus());
	}

	@Test
	void player2Wins() {
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		session = mutateSession.addPlayer2ToSession(session, player2.getUuid().toString(), player2.getName());

		// Test 1. Player 2 adds a move. Expected: the move is registered, the status is changed accordingly.
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.rock().getName()));

		assertNotNull(session.getMovePlayer2());
		assertNull(session.getMovePlayer1());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_PLAYER1_TO_MOVE, session.getStatus());

		// Test 2. Trying to evaluate the round before Player 1 adds a move. Expected: no change in status and in result.
		session = mutateSession.evaluateRound(session);

		assertNull(session.getMovePlayer1());
		assertNotNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_PLAYER1_TO_MOVE, session.getStatus());

		// Test 3. Player 1 adds a move. Expected: the move is registered, the status is changed accordingly.
		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.rock().getName()));

		assertNotNull(session.getMovePlayer1());
		assertNotNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(READY_FOR_ROUND_EVALUATION, session.getStatus());

		// Test 3. The round is evaluated. Expected: since it is a draw, the score is unchanged;
		// the status is changed accordingly  and the moves are unregistered.
		session = mutateSession.evaluateRound(session);
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(0, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_BOTH_PLAYERS_TO_MOVE, session.getStatus());

		// Test 4. This time Player 2 scores. Expected: score is updated;
		// the status is changed accordingly  and the moves are unregistered.
		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.paper().getName()));
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.scissors().getName()));
		session = mutateSession.evaluateRound(session);
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(1, session.getPlayer2().getScore());
		assertEquals(WAITING_FOR_BOTH_PLAYERS_TO_MOVE, session.getStatus());

		// Test 5. Player 2 scores other two times and reaches the maximum score. Expected:
		// Player 1 wins. The moves are unregistered and the status is changed accordingly.
		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.paper().getName()));
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.scissors().getName()));
		session = mutateSession.evaluateRound(session);

		session = mutateSession.addMovePlayer1(session, new Session.MovePlayer1(RPSLS.paper().getName()));
		session = mutateSession.addMovePlayer2(session, new Session.MovePlayer2(RPSLS.scissors().getName()));
		session = mutateSession.evaluateRound(session);

		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(0, session.getPlayer1().getScore());
		assertEquals(3, session.getPlayer2().getScore());
		assertEquals(PLAYER2_WON, session.getStatus());
	}

	@Test
	void player1CancelsTheSession() {
		// Test 1. Player 1 can cancel the session immediately after joining.
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		session = mutateSession.player1CancelsTheSession(session);
		assertSessionIsCancelledByPlayer1(session);

		// Test 2. Player 1 can cancel the session in a variety of legal statuses.
		List<Status> legalStatuses = Arrays.asList(WAITING_FOR_PLAYER2,
				PLAYER2_JOINED,
				WAITING_FOR_BOTH_PLAYERS_TO_MOVE,
				WAITING_FOR_PLAYER1_TO_MOVE,
				WAITING_FOR_PLAYER2_TO_MOVE,
				READY_FOR_ROUND_EVALUATION,
				ROUND_EVALUATED);

		final RPSLS sessionForLegalStatusTest = session;
		legalStatuses.stream()
				.map(status -> getSessionWithDesiredStatus(sessionForLegalStatusTest, status))
				.map(mutateSession::player1CancelsTheSession)
				.forEach(this::assertSessionIsCancelledByPlayer1);

		// Test 3. Player 2 cannot cancel the session in a variety of illegal statuses
		List<Status> illegalStatuses = Arrays.asList(PLAYER1_WON,
				PLAYER2_WON,
				CANCELLED_BY_SYSTEM,
				CANCELLED_BY_PLAYER2);

		final RPSLS sessionForIlegalStatusTest = session;
		illegalStatuses.stream()
				.map(status -> getSessionWithDesiredStatus(sessionForIlegalStatusTest, status))
				.map(mutateSession::player1CancelsTheSession)
				.forEach(this::assertSessionIsNotCancelledByPlayer1);
	}

	@Test
	void player2CancelsTheSession() {
		// Test 1. Player 2 can cancel the session immediately after joining.
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		session = mutateSession.player1CancelsTheSession(session);
		assertSessionIsCancelledByPlayer1(session);

		// Test 2. Player 2 can cancel the session in a variety of legal statuses.
		List<Status> legalStatuses = Arrays.asList(PLAYER2_JOINED,
				WAITING_FOR_BOTH_PLAYERS_TO_MOVE,
				WAITING_FOR_PLAYER1_TO_MOVE,
				WAITING_FOR_PLAYER2_TO_MOVE,
				READY_FOR_ROUND_EVALUATION,
				ROUND_EVALUATED);

		final RPSLS sessionForLegalStatusTest = session;
		legalStatuses.stream()
				.map(status -> getSessionWithDesiredStatus(sessionForLegalStatusTest, status))
				.map(mutateSession::player2CancelsTheSession)
				.forEach(this::assertSessionIsCancelledByPlayer2);

		// Test 3. Player 2 cannot cancel the session in a variety of illegal statuses
		List<Status> illegalStatuses = Arrays.asList(WAITING_FOR_PLAYER2,
				PLAYER1_WON,
				PLAYER2_WON,
				CANCELLED_BY_SYSTEM,
				CANCELLED_BY_PLAYER1);

		final RPSLS sessionForIlegalStatusTest = session;
		illegalStatuses.stream()
				.map(status -> getSessionWithDesiredStatus(sessionForIlegalStatusTest, status))
				.map(mutateSession::player2CancelsTheSession)
				.forEach(this::assertSessionIsNotCancelledByPlayer2);
	}

	@Test
	void systemCancelsTheSession() {
		// Test 1. System can cancel the session immediately after joining.
		RPSLS session = mutateSession.createSessionWithPlayer1(player1.getUuid().toString(), player1.getName());
		session = mutateSession.player1CancelsTheSession(session);
		assertSessionIsCancelledByPlayer1(session);

		// Test 2. System can cancel the session in a variety of legal statuses.
		List<Status> legalStatuses = Arrays.asList(WAITING_FOR_PLAYER2,
				PLAYER2_JOINED,
				WAITING_FOR_BOTH_PLAYERS_TO_MOVE,
				WAITING_FOR_PLAYER1_TO_MOVE,
				WAITING_FOR_PLAYER2_TO_MOVE,
				READY_FOR_ROUND_EVALUATION,
				ROUND_EVALUATED);

		final RPSLS sessionForLegalStatusTest = session;
		legalStatuses.stream()
				.map(status -> getSessionWithDesiredStatus(sessionForLegalStatusTest, status))
				.map(mutateSession::systemCancelsTheSession)
				.forEach(this::assertSessionIsCancelledBySystem);

		// Test 3. System cannot cancel the session in a variety of illegal statuses
		List<Status> illegalStatuses = Arrays.asList(PLAYER1_WON,
				PLAYER2_WON,
				CANCELLED_BY_PLAYER1,
				CANCELLED_BY_PLAYER2);

		final RPSLS sessionForIlegalStatusTest = session;
		illegalStatuses.stream()
				.map(status -> getSessionWithDesiredStatus(sessionForIlegalStatusTest, status))
				.map(mutateSession::systemCancelsTheSession)
				.forEach(this::assertSessionIsNotCancelledBySystem);
	}

	private void assertSessionIsCancelledByPlayer1(RPSLS session) {
		// Expected: moves are unregistered, status is set accordingly.
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(CANCELLED_BY_PLAYER1, session.getStatus());
	}

	private void assertSessionIsNotCancelledByPlayer1(RPSLS session) {
		assertNotEquals(CANCELLED_BY_PLAYER1, session.getStatus());
	}

	private void assertSessionIsCancelledByPlayer2(RPSLS session) {
		// Expected: moves are unregistered, status is set accordingly.
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(CANCELLED_BY_PLAYER2, session.getStatus());
	}

	private void assertSessionIsNotCancelledByPlayer2(RPSLS session) {
		assertNotEquals(CANCELLED_BY_PLAYER2, session.getStatus());
	}

	private void assertSessionIsCancelledBySystem(RPSLS session) {
		// Expected: moves are unregistered, status is set accordingly.
		assertNull(session.getMovePlayer1());
		assertNull(session.getMovePlayer2());
		assertEquals(CANCELLED_BY_SYSTEM, session.getStatus());
	}

	private void assertSessionIsNotCancelledBySystem(RPSLS session) {
		assertNotEquals(CANCELLED_BY_SYSTEM, session.getStatus());
	}

	private RPSLS getSessionWithDesiredStatus(RPSLS session, Status status) {
		return new RPSLS(session.getUuid(),
				session.getPlayer1(),
				session.getPlayer2(),
				session.getMovePlayer1(),
				session.getMovePlayer2(),
				status);
	}
}
