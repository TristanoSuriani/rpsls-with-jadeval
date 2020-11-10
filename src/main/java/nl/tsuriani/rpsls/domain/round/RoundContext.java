package nl.tsuriani.rpsls.domain.round;

import lombok.Builder;
import lombok.Getter;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;

@Builder
@Getter
public class RoundContext {
	@State
	private RoundState state;

	@Fact
	private Move movePlayer1;

	@Fact
	private Move movePlayer2;

	@Fact
	private int player1Score;

	@Fact
	private int player2Score;

	@Fact
	private RoundEvent roundEvent;
}
