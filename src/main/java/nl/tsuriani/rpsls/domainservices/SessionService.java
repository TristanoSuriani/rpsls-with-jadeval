package nl.tsuriani.rpsls.domainservices;

import nl.tsuriani.rpsls.domain.Session;

import java.util.List;

public interface SessionService {
	List<Session> findAll();

	void joinOrCreateSession(String playerUUID, String username);

	void cancelSession(String uuid, String playerUUID, String username);

	void chooseMove(String uuid, String playerUUID, String username, Session.Move move);

	void deleteAll();
}
