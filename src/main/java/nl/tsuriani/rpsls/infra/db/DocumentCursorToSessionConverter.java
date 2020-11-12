package nl.tsuriani.rpsls.infra.db;

import nl.tsuriani.rpsls.domain.PlayerIdentity;
import nl.tsuriani.rpsls.domain.SessionIdentity;
import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.domain.round.Round;
import nl.tsuriani.rpsls.domain.round.RoundState;
import nl.tsuriani.rpsls.domain.session.Player;
import nl.tsuriani.rpsls.domain.session.Session;
import nl.tsuriani.rpsls.domain.session.SessionState;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Document;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
public class DocumentCursorToSessionConverter {
	public List<Session> convert(Cursor cursor) {
		return StreamSupport.stream(cursor.spliterator(), false)
				.map(this::documentToSession)
				.collect(Collectors.toList());
	}

	private Session documentToSession(Document document) {
		return Session.builder()
				.identity(SessionIdentity.builder()
						.sessionUUID(getUUIDField(document, SESSION_UUID).orElse(null))
						.build())
				.state(getStringField(document, SESSION_STATE)
						.map(SessionState::valueOf)
						.orElse(null))
				.player1(documentToPlayer1(document))
				.player2(documentToPlayer2(document))
				.round(documentToRound(document))
				.build();
	}

	private Player documentToPlayer1(Document document) {
		return Player.builder()
				.identity(PlayerIdentity.builder()
						.clientUUID(getUUIDField(document, CLIENT_UUID_PLAYER1).orElse(null))
						.username(getStringField(document, USERNAME_PLAYER1).orElse(null))
						.build())
				.score(getIntField(document, SCORE_PLAYER1).orElse(0))
				.build();
	}

	private Player documentToPlayer2(Document document) {
		if (getUUIDField(document, CLIENT_UUID_PLAYER2).isEmpty()) {
			return null;
		}
		return Player.builder()
				.identity(PlayerIdentity.builder()
						.clientUUID(getUUIDField(document, CLIENT_UUID_PLAYER2).orElse(null))
						.username(getStringField(document, USERNAME_PLAYER2).orElse(null))
						.build())
				.score(getIntField(document, SCORE_PLAYER2).orElse(0))
				.build();
	}

	private Round documentToRound(Document document) {
		if (getStringField(document, ROUND_STATE).isEmpty()) {
			return null;
		}
		return Round.builder()
				.player1Move(getStringField(document, MOVE_PLAYER1)
					.map(Move::valueOf)
					.orElse(null))
				.player2Move(getStringField(document, MOVE_PLAYER2)
						.map(Move::valueOf)
						.orElse(null))
				.state(getStringField(document, ROUND_STATE)
						.map(RoundState::valueOf)
						.orElse(null))
				.build();
	}

	private Optional<String> getStringField(Document document, String fieldName) {
		var result = document.get(fieldName, String.class);
		return Optional.ofNullable(result);
	}

	private Optional<UUID> getUUIDField(Document document, String fieldName) {
		var result = document.get(fieldName, String.class);
		return Optional.ofNullable(result)
				.map(UUID::fromString);
	}

	private Optional<Integer> getIntField(Document document, String fieldName) {
		var result = document.get(fieldName, Integer.class);
		return Optional.ofNullable(result);
	}
}
