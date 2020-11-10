package nl.tsuriani.rpsls.infra.web.controller;

import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/session")
public class SessionController {
	@Inject
	SessionFacade sessionFacade;

	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> findSessions() {
		return sessionFacade.getAll();
	}

	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public String registerPlayer(SessionDTO.PlayerDTO playerDTO) {
		return sessionFacade.registerPlayer(playerDTO.getUuid(), playerDTO.getName());
	}
}
