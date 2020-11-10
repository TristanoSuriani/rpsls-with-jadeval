package nl.tsuriani.rpsls.infra.db;

import nl.tsuriani.rpsls.domain.SessionIdentity;
import nl.tsuriani.rpsls.domain.session.Session;
import nl.tsuriani.rpsls.domain.session.SessionState;
import org.dizitart.no2.Filter;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.UpdateOptions;
import org.dizitart.no2.filters.Filters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.SESSION_STATE;
import static nl.tsuriani.rpsls.infra.db.SessionDocumentConstants.SESSION_UUID;

@ApplicationScoped
public class SessionsRepositoryImpl {
	@Inject
	NitriteCollection sessions;

	@Inject
	DocumentCursorToSessionConverter documentCursorToSessionConverter;

	@Inject
	SessionToDocumentConverter sessionToDocumentConverter;

	public List<Session> findAll() {
		return documentCursorToSessionConverter.convert(sessions.find());
	}

	public Optional<Session> findBySessionIdentity(SessionIdentity sessionIdentity) {
		var filter = Filters.eq(SESSION_UUID, sessionIdentity.getSessionUUID().toString());
		return findWithFilter(filter);
	}

	public Optional<Session> findSessionWaitingForPlayer2() {
		var filter = Filters.eq(SESSION_STATE, SessionState.player1HasJoined.name());
		return findWithFilter(filter);
	}

	public void saveSession(Session session) {
		sessions.update(Filters.eq(SESSION_UUID, session.identity().getSessionUUID().toString()),
				sessionToDocumentConverter.convert(session),
				UpdateOptions.updateOptions(true));
	}

	private Optional<Session> findWithFilter(Filter filter) {
		return documentCursorToSessionConverter.convert(sessions.find(filter)).stream().findFirst();
	}
}
