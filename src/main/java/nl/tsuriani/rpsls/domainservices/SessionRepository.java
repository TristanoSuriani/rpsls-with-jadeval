package nl.tsuriani.rpsls.domainservices;

import nl.tsuriani.rpsls.domain.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository {
	Optional<Session> findOpenSession();

	List<Session> findAll();

	Optional<Session> findByUUID(String uuid);

	Optional<Session> findByPlayerUUIDAndName(String uuid, String name);

	void save(Session session);
 }
