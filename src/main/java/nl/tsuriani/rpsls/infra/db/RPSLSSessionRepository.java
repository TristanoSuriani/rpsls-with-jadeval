package nl.tsuriani.rpsls.infra.db;

import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domainservices.SessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RPSLSSessionRepository implements SessionRepository {
	@Override
	public Optional<Session> findOpenSession() {
		return SessionEntity.findOpenSession()
				.map(SessionEntity::fromEntity);
	}

	@Override
	public List<Session> findAll() {
		return SessionEntity.findAll().list().stream()
				.map(baseEntity -> (SessionEntity) baseEntity)
				.map(SessionEntity::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Session> findByUUID(String uuid) {
		return SessionEntity.findByUUID(uuid)
				.map(SessionEntity::fromEntity);
	}

	@Override
	public Optional<Session> findByPlayerUUIDAndName(String uuid, String name) {
		return SessionEntity.findByPlayerUUIDAndName(uuid, name)
				.map(SessionEntity::fromEntity);
	}

	@Override
	public void save(Session session) {
		SessionEntity.persistOrUpdate(new SessionEntity(session));
	}
}
