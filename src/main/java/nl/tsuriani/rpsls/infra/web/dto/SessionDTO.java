package nl.tsuriani.rpsls.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tsuriani.rpsls.domain.Session;

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
		this.movePlayer1 = session.getMovePlayer1() == null ? null : session.getMovePlayer1().getName();
		this.movePlayer2 = session.getMovePlayer2() == null ? null : session.getMovePlayer2().getName();
		this.status = session.getStatus() == null ? null : session.getStatus().name();
	}

	public static Session fromDTO(SessionDTO sessionDTO) {
		return new Session(sessionDTO.uuid == null ? null : UUID.fromString(sessionDTO.uuid),
				sessionDTO.player1 == null ? null : PlayerDTO.player1FromDTO(sessionDTO.player1),
				sessionDTO.player2 == null ? null : PlayerDTO.player2FromDTO(sessionDTO.player2),
				sessionDTO.movePlayer1 == null ? null : new Session.MovePlayer1(sessionDTO.movePlayer1.toUpperCase().trim()),
				sessionDTO.movePlayer2 == null ? null : new Session.MovePlayer2(sessionDTO.movePlayer2.toUpperCase().trim()),
				sessionDTO.status == null ? null : Session.Status.valueOf(sessionDTO.status.toUpperCase().trim()));
	}

	@AllArgsConstructor
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
