package nl.tsuriani.rpsls.infra.web.controller;

import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/api/test")
public class TestController {
	@Inject
	SessionFacade sessionFacade;

	@Inject
	NitriteCollection sessions;

	@Path("happyFlow1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> happyFlow1() {
		SessionDTO.PlayerDTO player1 = new SessionDTO.PlayerDTO(UUID.randomUUID().toString(), "Frank", 0);
		SessionDTO.PlayerDTO player2 = new SessionDTO.PlayerDTO(UUID.randomUUID().toString(), "Harco", 0);

		sessions.remove(Filters.ALL);
		sessionFacade.getAll();
		sessionFacade.registerPlayer(player1.getUuid(), player1.getName());
		sessionFacade.getAll();
		sessionFacade.registerPlayer(player2.getUuid(), player2.getName());
		return sessionFacade.getAll();
	}
}
