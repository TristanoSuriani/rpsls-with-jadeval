package nl.tsuriani.rpsls.domain.session;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nl.tsuriani.rpsls.domain.SessionIdentity;
import nl.tsuriani.rpsls.domain.round.Round;

import java.util.Optional;

@Builder
@Data
@Accessors(fluent = true)
public class Session {
	private SessionIdentity identity;
	private SessionState state;
	private Player player1;
	private Player player2;
	private Round round;

	public Optional<Player> getPlayer2() {
		return Optional.ofNullable(player2);
	}

	public Optional<Round> getRound() {
		return Optional.ofNullable(round);
	}
}
