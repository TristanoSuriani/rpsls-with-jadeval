package nl.tsuriani.rpsls.infra.web.controller;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.infra.facade.SessionFacade;
import nl.tsuriani.rpsls.infra.web.dto.ChoiceDTO;
import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Path("/api/session")
public class SessionController {

	private SessionFacade sessionFacade;

	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> get(@QueryParam("clientuuid") String clientUUID, @QueryParam("username") String username) {
		if (clientUUID == null || username == null) {
			return sessionFacade.get();
		} else {
			return new ArrayList<>(); // specific sessions
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SessionDTO registerPlayerToSession(SessionDTO.PlayerDTO player) {
		return sessionFacade.registerPlayer(player); // register
	}

	@DELETE
	public void delete() {
		sessionFacade.deleteAll();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{uuid}")
	public SessionDTO get(@PathParam("uuid") String uuid) {
		return null; // specific session
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{sessionUUID}")
	public void sendChoice(ChoiceDTO choice, @PathParam("sessionUUID") String sessionUUID) {
		sessionFacade.sendChoice(choice); // session
	}
}
