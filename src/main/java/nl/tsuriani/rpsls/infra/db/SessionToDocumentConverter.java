package nl.tsuriani.rpsls.infra.db;

import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.domain.round.Round;
import nl.tsuriani.rpsls.domain.round.RoundState;
import nl.tsuriani.rpsls.domain.session.Player;
import nl.tsuriani.rpsls.domain.session.Session;
import org.dizitart.no2.Document;

import javax.enterprise.context.ApplicationScoped;

import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.CLIENT_UUID_PLAYER1;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.CLIENT_UUID_PLAYER2;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.MOVE_PLAYER1;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.MOVE_PLAYER2;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.ROUND_STATE;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.SCORE_PLAYER1;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.SCORE_PLAYER2;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.SESSION_STATE;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.SESSION_UUID;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.USERNAME_PLAYER1;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.USERNAME_PLAYER2;

@ApplicationScoped
public class SessionToDocumentConverter {
	public Document convert(Session session) {
		return new Document()
				.put(SESSION_UUID, session.identity().getSessionUUID().toString())
				.put(CLIENT_UUID_PLAYER1, session.player1().getIdentity().getClientUUID().toString())
				.put(USERNAME_PLAYER1, session.player1().getIdentity().getUsername())
				.put(SCORE_PLAYER1, session.player1().getScore())
				.put(CLIENT_UUID_PLAYER2, session.getPlayer2()
						.map(player -> player.getIdentity().getClientUUID().toString())
						.orElse(null))
				.put(USERNAME_PLAYER2, session.getPlayer2()
						.map(player -> player.getIdentity().getUsername())
						.orElse(null))
				.put(SCORE_PLAYER2, session.getPlayer2()
						.map(Player::getScore)
						.orElse(0))
				.put(SESSION_STATE, session.state().name())
				.put(MOVE_PLAYER1, session.getRound()
						.flatMap(Round::getPlayer1Move)
						.map(Move::name)
						.orElse(null))
				.put(MOVE_PLAYER2, session.getRound()
						.flatMap(Round::getPlayer2Move)
						.map(Move::name)
						.orElse(null))
				.put(ROUND_STATE, session.getRound()
						.flatMap(Round::getState)
						.map(RoundState::name)
						.orElse(null));


	}
}
