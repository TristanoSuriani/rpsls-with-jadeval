package nl.tsuriani.rpsls.infra.service;

import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.domain.session.Session;

import java.util.List;

public interface SessionService {
	List<Session> findAll();

	Session joinOrCreateSession(String playerUUID, String username);

	void cancelSession(String uuid, String playerUUID, String username);

	void chooseMove(String uuid, String playerUUID, String username, Move move);

	void deleteAll();
}
