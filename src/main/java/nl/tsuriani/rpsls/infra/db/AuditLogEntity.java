package nl.tsuriani.rpsls.infra.db;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@MongoEntity(collection = "log")
public class AuditLogEntity extends PanacheMongoEntity {
	private String sessionUUID;
	private String status;
	private String userEvent;
	private String systemEvent;
	private String movePlayer1;
	private String movePlayer2;
	private int scorePlayer1;
	private int scorePlayer2;
	private String roundResult;
	private LocalDateTime dateLogged;
}
