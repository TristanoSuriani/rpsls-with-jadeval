package nl.tsuriani.rpsls.domain.session;

import lombok.Builder;
import lombok.Getter;
import nl.tsuriani.rpsls.domain.PlayerIdentity;

import java.util.UUID;

@Builder
@Getter
public class Player {
	private PlayerIdentity identity;
	private int score;

	public Player incrementScore() {
		return Player.builder()
				.identity(identity)
				.score(score + 1)
				.build();
	}

	public boolean isSamePlayer(PlayerIdentity playerIdentity) {
		return this.identity.getUsername().equals(playerIdentity.getUsername()) &&
				this.identity.getClientUUID().equals(playerIdentity.getClientUUID());
	}

	public boolean isSamePlayer(String clientUUID, String username) {
		return isSamePlayer(PlayerIdentity.builder()
				.clientUUID(UUID.fromString(clientUUID))
				.username(username)
				.build());
	}
}
