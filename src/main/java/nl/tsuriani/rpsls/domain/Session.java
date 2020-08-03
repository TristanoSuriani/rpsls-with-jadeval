package nl.tsuriani.rpsls.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Session {

	private UUID uuid;
	private Player1 player1;
	private Player2 player2;
	private MovePlayer1 movePlayer1;
	private MovePlayer2 movePlayer2;
	private Status status;

	@AllArgsConstructor
	@EqualsAndHashCode
	@Getter
	public static abstract class Player {
		private UUID uuid;
		private String name;
		private int score = 0;
	}

	public static final class Player1 extends Player {

		public Player1(UUID uuid, String name, int score) {
			super(uuid, name, score);
		}

		public Player1(UUID uuid, String name) {
			super(uuid, name, 0);
		}
	}

	public static final class Player2 extends Player {

		public Player2(UUID uuid, String name, int score) {
			super(uuid, name, score);
		}

		public Player2(UUID uuid, String name) {
			super(uuid, name, 0);
		}
	}

	@AllArgsConstructor
	@EqualsAndHashCode
	@Getter
	public static class Move {
		private String name;
	}

	public static final class MovePlayer1 extends Move {

		public MovePlayer1(String name) {
			super(name);
		}
	}

	public static final class MovePlayer2 extends Move {

		public MovePlayer2(String name) {
			super(name);
		}
	}

	public enum Status {
		WAITING_FOR_PLAYER2,
		PLAYER2_JOINED,
		WAITING_FOR_BOTH_PLAYERS_TO_MOVE,
		WAITING_FOR_PLAYER1_TO_MOVE,
		WAITING_FOR_PLAYER2_TO_MOVE,
		READY_FOR_ROUND_EVALUATION,
		ROUND_EVALUATED,
		PLAYER1_WON,
		PLAYER2_WON,
		CANCELLED_BY_SYSTEM,
		CANCELLED_BY_PLAYER1,
		CANCELLED_BY_PLAYER2
	}
}
