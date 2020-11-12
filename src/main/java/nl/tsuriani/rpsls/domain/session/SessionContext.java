package nl.tsuriani.rpsls.domain.session;

import lombok.Builder;
import lombok.Getter;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;

@Builder
@Getter
public class SessionContext {
	@Fact
	private UserAction userAction;

	@Fact
	private GameEvent gameEvent;

	@State
	private SessionState sessionState;

	public enum UserAction {
		JOIN, DISCONNECT
	}

	public enum GameEvent {
		PLAYER1_WINS, PLAYER2_WINS
	}
}
