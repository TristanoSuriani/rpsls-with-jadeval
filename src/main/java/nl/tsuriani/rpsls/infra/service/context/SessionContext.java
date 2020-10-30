package nl.tsuriani.rpsls.infra.service.context;

import lombok.Builder;
import lombok.Data;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.annotation.State;
import nl.tsuriani.rpsls.domain.Session;
import nl.tsuriani.rpsls.domain.SystemEvent;
import nl.tsuriani.rpsls.domain.UserEvent;

@Builder
@Data
public class SessionContext {
	private Session session;

	@State
	private Session.Status status;

	@Fact
	private int player1Score;

	@Fact
	private int player2Score;

	@Fact
	private UserEvent userEvent;

	@Fact
	private SystemEvent systemEvent;

	public SessionContext session(Session session) {
		this.session = session;
		if (session.getPlayer1() != null) {
			player1Score = session.getPlayer1().getScore();
		}
		if (session.getPlayer2() != null) {
			player2Score = session.getPlayer2().getScore();
		}
		return this;
	}
}
