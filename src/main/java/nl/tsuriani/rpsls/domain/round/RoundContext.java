package nl.tsuriani.rpsls.domain.round;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;

@Builder
@Data
@Accessors(fluent = true)
public class RoundContext {
	@State
	private RoundState state;

	@Fact
	private Move player1Move;

	@Fact
	private Move player2Move;

	@Fact
	private int player1Score;

	@Fact
	private int player2Score;

	@Fact
	private RoundEvent roundEvent;
}
