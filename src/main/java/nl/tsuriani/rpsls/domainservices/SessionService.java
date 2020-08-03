package nl.tsuriani.rpsls.domainservices;

import nl.tsuriani.rpsls.domain.Session;

public interface SessionService {
	Session joinOrCreateSession(String playerUUID, String username);

	void cancelSession(String uuid, String playerUUID, String username);

	void chooseMove(String uuid, String playerUUID, String username, Session.Move move);
}
