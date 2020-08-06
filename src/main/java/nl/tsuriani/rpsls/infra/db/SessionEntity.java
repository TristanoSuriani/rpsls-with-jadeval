package nl.tsuriani.rpsls.infra.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

	public SessionEntity(Session session) {
		merge(session);
	}

	public void merge(Session session) {
		this.uuid = session.getUuid() == null ? null : session.getUuid().toString();
		this.player1 = session.getPlayer1() == null ? null : new PlayerEntity(session.getPlayer1());
		this.player2 = session.getPlayer2() == null ? null : new PlayerEntity(session.getPlayer2());
		this.movePlayer1 = session.getMovePlayer1() == null ? null : session.getMovePlayer1().getName();
		this.movePlayer2 = session.getMovePlayer2() == null ? null : session.getMovePlayer2().getName();
		this.status = session.getStatus() == null ? null : session.getStatus().name();
	}

	public static Session fromEntity(SessionEntity sessionEntity) {
		return new Session(sessionEntity.uuid == null ? null : UUID.fromString(sessionEntity.uuid),
				sessionEntity.player1 == null ? null : PlayerEntity.player1FromEntity(sessionEntity.player1),
				sessionEntity.player2 == null ? null : PlayerEntity.player2FromEntity(sessionEntity.player2),
				sessionEntity.movePlayer1 == null ? null : new Session.MovePlayer1(sessionEntity.movePlayer1.toUpperCase().trim()),
				sessionEntity.movePlayer2 == null ? null : new Session.MovePlayer2(sessionEntity.movePlayer2.toUpperCase().trim()),
				sessionEntity.status == null ? null : Session.Status.valueOf(sessionEntity.status.toUpperCase().trim()));
	}

	public static Optional<SessionEntity> findOpenSession(){
		return Optional.ofNullable(find("status", "WAITING_FOR_PLAYER2").firstResult());
	}

	public static Optional<SessionEntity> findByUUID(String uuid){
		return Optional.ofNullable(find("uuid", uuid).firstResult());
	}

	public static Optional<SessionEntity> findByPlayerUUIDAndName(String uuid, String name){
		Map<String, Object> params = new HashMap<>();
		params.put("uuid", uuid);
		params.put("name", name);

		SessionEntity result =  SessionEntity.find("player1.uuid = :uuid and player1.name = :name or " +
					"player2.uuid = :uuid and player2.name = :name", params).firstResult();

		return Optional.ofNullable(result);
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

		public static Session.Player1 player1FromEntity(PlayerEntity playerEntity) {
			return new Session.Player1(UUID.fromString(playerEntity.uuid), playerEntity.name, playerEntity.score);
		}

		public static Session.Player2 player2FromEntity(PlayerEntity playerEntity) {
			return new Session.Player2(UUID.fromString(playerEntity.uuid), playerEntity.name, playerEntity.score);
		}
	}
}
