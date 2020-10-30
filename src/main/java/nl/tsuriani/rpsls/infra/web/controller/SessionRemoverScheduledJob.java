package nl.tsuriani.rpsls.infra.web.controller;

import io.quarkus.scheduler.Scheduled;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.infra.db.AuditLogEntity;
import nl.tsuriani.rpsls.infra.db.SessionEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class SessionRemoverScheduledJob {
	private static List<String> finalStates;
	static {
		finalStates = Stream.of(Session.Status.player1HasWon,
			Session.Status.player2HasWon, Session.Status.player1IsDisconnected,
			Session.Status.player2IsDisconnected)
				.map(Enum::name)
				.collect(Collectors.toList());

	}

	@Scheduled(every = "15s")
	public void removeTerminatedSessions() {
		SessionEntity.findAll().list().stream()
				.map(sessionEntity -> (SessionEntity) sessionEntity)
				.filter(sessionEntity -> finalStates.contains(sessionEntity.getStatus()))
				.peek(SessionEntity::delete)
				.map(SessionEntity::getUuid)
				.forEach(AuditLogEntity::deleteBySessionUUID);
	}

}
