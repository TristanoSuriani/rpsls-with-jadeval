package nl.tsuriani.rpsls.infra.web.controller;

import nl.tsuriani.rpsls.domain.round.Move;
import nl.tsuriani.rpsls.infra.service.RPSLSSessionService;
import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SessionFacade {
	@Inject
	RPSLSSessionService rpslsSessionService;

	public List<SessionDTO> getAll() {
		return rpslsSessionService.findAll().stream()
				.map(SessionDTO::new)
				.collect(Collectors.toList());
	}

	public String registerPlayer(String clientUUID, String username) {
		return rpslsSessionService.joinOrCreateSession(clientUUID, username).identity().getSessionUUID().toString();
	}

	public void chooseMove(String sessionUUID, String clientUUID, String username, Move move) {
		rpslsSessionService.chooseMove(sessionUUID, clientUUID, username, move);
	}

	public void disconnect(String sessionUUID) {
		rpslsSessionService.cancelSession(sessionUUID);
	}
}
