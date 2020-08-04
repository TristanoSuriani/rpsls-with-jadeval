package nl.tsuriani.rpsls.infra.web.dto;

import nl.tsuriani.rpsls.domain.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SessionDTOTest {
	@Test
	void testEmptyValuesShouldNotRaiseNullPointers() {
		Session session1 = new Session(null, null, null, null, null, null);
		SessionDTO sessionDTO = new SessionDTO(session1);
		Session session2 = SessionDTO.fromDTO(sessionDTO);

		Assertions.assertAll(() -> {
			assertNotNull(sessionDTO);
			assertNull(sessionDTO.getUuid());
			assertNull(sessionDTO.getPlayer1());
			assertNull(sessionDTO.getPlayer2());
			assertNull(sessionDTO.getMovePlayer1());
			assertNull(sessionDTO.getMovePlayer2());
			assertNull(sessionDTO.getStatus());

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
