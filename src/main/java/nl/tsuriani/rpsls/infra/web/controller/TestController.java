package nl.tsuriani.rpsls.infra.web.controller;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.infra.db.AuditLogEntity;
import nl.tsuriani.rpsls.infra.facade.SessionFacade;
import nl.tsuriani.rpsls.infra.web.dto.ChoiceDTO;
import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/api/test")
@AllArgsConstructor
public class TestController {
	private SessionFacade sessionFacade;

	@Path("/auditlog")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<AuditLogEntity> log() {
		return AuditLogEntity.findAll()
				.list();
	}

	@Path("/happyflow1")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> happyflow1() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "Jack"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "Jill"));
		sessionFacade.deleteAll();
		sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);

		var session = sessionFacade.get().get(0);
		var choiceDTOP1 = ChoiceDTO.builder()
				.sessionUUID(session.getUuid())
				.clientUUID(player1.getUuid())
				.username(player1.getName())
				.move("ROCK")
				.build();

		var choiceDTOP2 = ChoiceDTO.builder()
				.sessionUUID(session.getUuid())
				.clientUUID(player2.getUuid())
				.username(player2.getName())
				.move("SCISSORS")
				.build();

		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.disconnectPlayer(session, player1);
		sessionFacade.disconnectPlayer(session, player2);
		return sessionFacade.get();
	}

	@Path("/happyflow2")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> happyflow2() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "Jack"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "Jill"));
		sessionFacade.deleteAll();
		sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);
		var session = sessionFacade.get().get(0);
		var choiceDTOP1 = ChoiceDTO.builder()
				.sessionUUID(session.getUuid())
				.clientUUID(player1.getUuid())
				.username(player1.getName())
				.move("LIZARD")
				.build();

		var choiceDTOP2 = ChoiceDTO.builder()
				.sessionUUID(session.getUuid())
				.clientUUID(player2.getUuid())
				.username(player2.getName())
				.move("SCISSORS")
				.build();

		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		return sessionFacade.get();
	}

	@Path("/unhappyflow")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> unhappyFlow() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "Jack"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "Jill"));
		sessionFacade.deleteAll();
		sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);
		var session = sessionFacade.get().get(0);
		var choiceDTOP1 = ChoiceDTO.builder()
				.sessionUUID(session.getUuid())
				.clientUUID(player1.getUuid())
				.username(player1.getName())
				.move("ROCK")
				.build();

		var choiceDTOP2 = ChoiceDTO.builder()
				.sessionUUID(session.getUuid())
				.clientUUID(player2.getUuid())
				.username(player2.getName())
				.move("ROCK")
				.build();

		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.sendChoice(choiceDTOP1);
		sessionFacade.sendChoice(choiceDTOP2);
		sessionFacade.disconnectPlayer(session, player2);
		return sessionFacade.get();
	}
}
