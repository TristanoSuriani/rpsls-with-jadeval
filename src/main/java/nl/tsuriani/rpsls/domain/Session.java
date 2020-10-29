package nl.tsuriani.rpsls.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class Session {

	private UUID uuid;
	private Player1 player1;
	private Player2 player2;
	private Move movePlayer1;
	private Move movePlayer2;
	private Status status;

	@AllArgsConstructor
	@EqualsAndHashCode
	@Getter
	public static abstract class Player {
		private UUID uuid;
		private String name;
		private int score = 0;

		public void incrementScore() {
			score += 1;
		}
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

	public enum Move {
		ROCK, PAPER, SCISSORS, LIZARD, SPOCK
	}

	public enum Status {
		waitingForPlayer1ToJoin,
		waitingForPlayer2ToJoin, bothPlayersJoined,
		waitingForBothPlayersToChoose, waitingForPlayer1ToChoose, waitingForPlayer2ToChoose,
		bothPlayersHaveChosen, player1HasScored, player2HasScored, nobodyHasScored,
		player1HasWon, player2HasWon, player1IsDisconnected, player2IsDisconnected
	}
}
