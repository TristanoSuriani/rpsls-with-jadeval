package nl.tsuriani.rpsls.infra.db;

import nl.tsuriani.rpsls.applicationservices.context.SessionContextFactory;
import nl.tsuriani.rpsls.domain.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SessionEntityTest {
	@Test
	void testEmptyValuesShouldNotRaiseNullPointers() {
		Session session1 = Session.builder().build();
		var sessionContext = new SessionContextFactory().makeSessionContextWithoutInput(session1);
		SessionEntity sessionEntity = new SessionEntity(sessionContext);
		Session session2 = SessionEntity.toSession(sessionEntity);

		Assertions.assertAll(() -> {
			assertNotNull(sessionEntity);
			assertNull(sessionEntity.getUuid());
			assertNull(sessionEntity.getPlayer1());
			assertNull(sessionEntity.getPlayer2());
			assertNull(sessionEntity.getMovePlayer1());
			assertNull(sessionEntity.getMovePlayer2());
			assertNull(sessionEntity.getStatus());

			assertNotNull(session2);
			assertNull(session2.getUuid());
			assertNull(session2.getPlayer1());
			assertNull(session2.getPlayer2());
			assertNull(session2.getMovePlayer1());
			assertNull(session2.getMovePlayer2());
			assertNull(session2.getStatus());

		});
	}

}
