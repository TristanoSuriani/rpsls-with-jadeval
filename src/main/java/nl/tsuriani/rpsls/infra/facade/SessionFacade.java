package nl.tsuriani.rpsls.infra.facade;

import lombok.AllArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domainservices.SessionService;
import nl.tsuriani.rpsls.infra.web.dto.ChoiceDTO;
import nl.tsuriani.rpsls.infra.web.dto.SessionDTO;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SessionFacade {
	private SessionService sessionService;

	public List<SessionDTO> get() {
		return sessionService.findAll().stream()
				.map(SessionDTO::new)
				.collect(Collectors.toList());
	}

	public void registerPlayer(SessionDTO.PlayerDTO player) {
		sessionService.joinOrCreateSession(player.getUuid(), player.getName());
	}

	public void deleteAll() {
		sessionService.deleteAll();
	}

	public void sendChoice(ChoiceDTO choice) {
		sessionService.chooseMove(choice.sessionUUID, choice.clientUUID, choice.username, new Session.Move(choice.move));
	}
}
