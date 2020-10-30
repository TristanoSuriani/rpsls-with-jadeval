package nl.tsuriani.rpsls.infra.service;

import nl.tsuriani.rpsls.infra.service.context.SessionContext;
import nl.tsuriani.rpsls.domain.Session;

import java.util.List;

public interface SessionService {
	List<Session> findAll();

	SessionContext joinOrCreateSession(String playerUUID, String username);

	void cancelSession(String uuid, String playerUUID, String username);

	void chooseMove(String uuid, String playerUUID, String username, Session.Move move);

	void deleteAll();
}
