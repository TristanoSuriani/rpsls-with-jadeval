package nl.tsuriani.rpsls.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tsuriani.rpsls.domain.PlayerIdentity;
import nl.tsuriani.rpsls.domain.SessionIdentity;
import nl.tsuriani.rpsls.domain.session.Player;
import nl.tsuriani.rpsls.domain.session.Session;
import nl.tsuriani.rpsls.domain.session.SessionState;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Data
public class SessionDTO {
	public String sessionUUID;
	public PlayerDTO player1;
	public PlayerDTO player2;
	public String movePlayer1;
	public String movePlayer2;
	public String sessionState;

	public SessionDTO(Session session) {
		this.sessionUUID = session.identity().getSessionUUID().toString();
		this.player1 = new PlayerDTO(session.player1());
		this.player2 = session.getPlayer2()
				.map(PlayerDTO::new)
				.orElse(null);
		this.sessionState = session.state().name();
	}

	public Session toSession() {
		return Session.builder()
				.identity(SessionIdentity.builder()
						.sessionUUID(UUID.fromString(sessionState))
						.build())
				.state(SessionState.valueOf(sessionState))
				.player1(Optional.ofNullable(player1)
						.map(PlayerDTO::toPlayer)
						.orElse(null))
				.player2(Optional.ofNullable(player2)
						.map(PlayerDTO::toPlayer)
						.orElse(null))
				.build();
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class PlayerDTO {
		private String uuid;
		private String name;
		private int score = 0;

		public PlayerDTO(Player player) {
			this.uuid = player.getIdentity().getClientUUID().toString();
			this.name = player.getIdentity().getUsername();
			this.score = player.getScore();
		}

		public Player toPlayer() {
			return Player.builder()
					.identity(PlayerIdentity.builder()
							.clientUUID(UUID.fromString(uuid))
							.username(name)
							.build())
					.score(score)
					.build();
		}
	}
}
