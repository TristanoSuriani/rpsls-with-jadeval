package nl.tsuriani.rpsls.domain.round;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Round {
	private Move movePlayer1;
	private Move movePlayer2;
	private RoundState state;
}
