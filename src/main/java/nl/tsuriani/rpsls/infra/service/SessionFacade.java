package nl.tsuriani.rpsls.infra.service;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.infra.web.dto.ChoiceDTO;
import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class SessionFacade {
	private SessionService sessionService;

	public List<SessionDTO> get() {
		return sessionService.findAll().stream()
				.map(SessionDTO::new)
				.collect(Collectors.toList());
	}

	public SessionDTO registerPlayer(SessionDTO.PlayerDTO player) {
		return new SessionDTO(sessionService.joinOrCreateSession(player.getUuid(), player.getName()).getSession());
	}

	public void deleteAll() {
		sessionService.deleteAll();
	}

	public void sendChoice(ChoiceDTO choice) {
		sessionService.chooseMove(choice.sessionUUID, choice.clientUUID, choice.username, Session.Move.valueOf(choice.move));
	}

	public void disconnectPlayer(SessionDTO sessionDTO, SessionDTO.PlayerDTO playerDTO) {
		sessionService.cancelSession(sessionDTO.getUuid(), playerDTO.getUuid(), playerDTO.getName());
	}
}
