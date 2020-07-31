package nl.tsuriani.rpsls.domain;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class RPSLS extends Session {
	public static final String ROCK = "ROCK";
	public static final String PAPER = "PAPER";
	public static final String SCISSORS = "SCISSORS";
	public static final String LIZARD = "LIZARD";
	public static final String SPOCK = "SPOCK";

	public RPSLS(UUID uuid, Player1 player1, Player2 player2, MovePlayer1 movePlayer1, MovePlayer2 movePlayer2, Status status) {
		super(uuid, player1, player2, movePlayer1, movePlayer2, status);
	}

	public RPSLS(RPSLS session) {
		super(session.getUuid(), session.getPlayer1(), session.getPlayer2(), session.getMovePlayer1(), session.getMovePlayer2(), session.getStatus());
	}

	public static boolean isValidMove(Move move) {
		return availableMoves().contains(move);
	}

	public static Move rock() {
		return new Move(ROCK);
	}

	public static Move paper() {
		return new Move(PAPER);
	}

	public static Move scissors() {
		return new Move(SCISSORS);
	}

	public static Move lizard() {
		return new Move(LIZARD);
	}

	public static Move spock() {
		return new Move(SPOCK);
	}

	private static List<Move> availableMoves() {
		return Arrays.asList(rock(), paper(), scissors(), lizard(), spock());
	}
}
