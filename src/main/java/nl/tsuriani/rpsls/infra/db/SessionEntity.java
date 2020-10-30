package nl.tsuriani.rpsls.infra.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tsuriani.rpsls.infra.service.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MongoEntity(collection = "sessions")
public class SessionEntity extends PanacheMongoEntity {

	private String uuid;
	private PlayerEntity player1;
	private PlayerEntity player2;
	private String movePlayer1;
	private String movePlayer2;
	private String status;

	public SessionEntity(SessionContext sessionContext) {
		merge(sessionContext);
	}

	public void merge(SessionContext sessionContext) {
		var session = sessionContext.getSession();
		this.uuid = session.getUuid() == null ? null : session.getUuid().toString();
		this.player1 = session.getPlayer1() == null ? null : new PlayerEntity(session.getPlayer1());
		this.player2 = session.getPlayer2() == null ? null : new PlayerEntity(session.getPlayer2());
		this.movePlayer1 = session.getMovePlayer1() == null ? null : session.getMovePlayer1().name();
		this.movePlayer2 = session.getMovePlayer2() == null ? null : session.getMovePlayer2().name();
		this.status = sessionContext.getStatus() == null ? null : sessionContext.getStatus().name();
	}

	public static Session toSession(SessionEntity sessionEntity) {
		return Session.builder()
				.uuid(sessionEntity.uuid == null ? null : UUID.fromString(sessionEntity.uuid))
				.player1(sessionEntity.player1 == null ? null : PlayerEntity.player1ToSession(sessionEntity.player1))
				.player2(sessionEntity.player2 == null ? null : PlayerEntity.player2ToSession(sessionEntity.player2))
				.movePlayer1(sessionEntity.movePlayer1 == null ? null : Session.Move.valueOf(sessionEntity.movePlayer1.toUpperCase().trim()))
				.movePlayer2(sessionEntity.movePlayer2 == null ? null : Session.Move.valueOf(sessionEntity.movePlayer2.toUpperCase().trim()))
				.status(sessionEntity.status == null ? null : Session.Status.valueOf(sessionEntity.status))
				.build();
	}

	public static Optional<SessionEntity> findOpenSession(){
		return Optional.ofNullable(find("status", Session.Status.waitingForPlayer2ToJoin).firstResult());
	}

	public static Optional<SessionEntity> findByUUID(String uuid) {
		Map<String, Object> params = new HashMap<>();
		params.put("uuid", uuid);
		return Optional.ofNullable(find("uuid = :uuid", params).firstResult());
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class PlayerEntity {
		private String uuid;
		private String name;
		private int score = 0;

		public PlayerEntity(Session.Player player) {
			this.uuid = player.getUuid().toString();
			this.name = player.getName();
			this.score = player.getScore();
		}

		public static Session.Player1 player1ToSession(PlayerEntity playerEntity) {
			return new Session.Player1(UUID.fromString(playerEntity.uuid), playerEntity.name, playerEntity.score);
		}

		public static Session.Player2 player2ToSession(PlayerEntity playerEntity) {
			return new Session.Player2(UUID.fromString(playerEntity.uuid), playerEntity.name, playerEntity.score);
		}
	}
}
