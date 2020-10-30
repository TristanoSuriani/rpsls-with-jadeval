package nl.tsuriani.rpsls.infra.web.controller;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.infra.db.AuditLogEntity;
import nl.tsuriani.rpsls.infra.service.SessionFacade;
import nl.tsuriani.rpsls.infra.web.dto.ChoiceDTO;
import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

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

	@Path("/player1wins")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> player1Wins() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "Jack"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "Jill"));
		sessionFacade.deleteAll();
		var session = sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);

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

	@Path("/player2wins")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> player2Wins() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "John"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "James"));
		sessionFacade.deleteAll();
		var session = sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);
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

	@Path("/player2disconnects")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> player2Disconnects() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "Jack"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "Jill"));
		sessionFacade.deleteAll();
		var session = sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);
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

	@Path("/notterminated")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<SessionDTO> notTerminated() {
		var player1 = new SessionDTO.PlayerDTO(new Session.Player1(UUID.randomUUID(), "Jack"));
		var player2 = new SessionDTO.PlayerDTO(new Session.Player2(UUID.randomUUID(), "Jill"));
		sessionFacade.deleteAll();
		var session = sessionFacade.registerPlayer(player1);
		sessionFacade.registerPlayer(player2);
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
		return sessionFacade.get();
	}
}
