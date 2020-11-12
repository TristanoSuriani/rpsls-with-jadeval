package nl.tsuriani.rpsls.domain.round;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Optional;

@Builder
@Getter
@Accessors(fluent = true)
public class Round {
	private Move player1Move;
	private Move player2Move;
	private RoundState state;

	public Optional<Move> getPlayer1Move() {
		return Optional.ofNullable(player1Move);
	}

	public Optional<Move> getPlayer2Move() {
		return Optional.ofNullable(player2Move);
	}

	public Optional<RoundState> getState() {
		return Optional.ofNullable(state);
	}
}
