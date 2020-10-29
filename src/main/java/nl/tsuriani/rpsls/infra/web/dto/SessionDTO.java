package nl.tsuriani.rpsls.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.infra.db.SessionEntity;

import java.util.UUID;

@AllArgsConstructor
@Data
public class SessionDTO {

	private String uuid;
	private PlayerDTO player1;
	private PlayerDTO player2;
	private String movePlayer1;
	private String movePlayer2;
	private String status;

	public SessionDTO(Session session) {
		this.uuid = session.getUuid() == null ? null : session.getUuid().toString();
		this.player1 = session.getPlayer1() == null ? null : new PlayerDTO(session.getPlayer1());
		this.player2 = session.getPlayer2() == null ? null : new PlayerDTO(session.getPlayer2());
		this.movePlayer1 = session.getMovePlayer1() == null ? null : session.getMovePlayer1().name();
		this.movePlayer2 = session.getMovePlayer2() == null ? null : session.getMovePlayer2().name();
		this.status = session.getStatus() == null ? null : session.getStatus().name();
	}

	public static Session toSession(SessionDTO sessionDTO) {
		return Session.builder()
				.uuid(sessionDTO.uuid == null ? null : UUID.fromString(sessionDTO.uuid))
				.player1(sessionDTO.player1 == null ? null : PlayerDTO.player1FromDTO(sessionDTO.player1))
				.player2(sessionDTO.player2 == null ? null : PlayerDTO.player2FromDTO(sessionDTO.player2))
				.movePlayer1(sessionDTO.movePlayer1 == null ? null : Session.Move.valueOf(sessionDTO.movePlayer1.toUpperCase().trim()))
				.movePlayer1(sessionDTO.movePlayer2 == null ? null : Session.Move.valueOf(sessionDTO.movePlayer1.toUpperCase().trim()))
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

		public PlayerDTO(Session.Player player) {
			this.uuid = player.getUuid().toString();
			this.name = player.getName();
			this.score = player.getScore();
		}

		public static Session.Player1 player1FromDTO(PlayerDTO playerDTO) {
			return new Session.Player1(UUID.fromString(playerDTO.uuid), playerDTO.name, playerDTO.score);
		}

		public static Session.Player2 player2FromDTO(PlayerDTO playerDTO) {
			return new Session.Player2(UUID.fromString(playerDTO.uuid), playerDTO.name, playerDTO.score);
		}
	}
}
