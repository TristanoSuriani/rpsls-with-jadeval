package nl.tsuriani.rpsls.infra.service.context;

import lombok.Builder;
import nl.suriani.jadeval.annotation.Fact;
import nl.tsuriani.rpsls.domain.Session;

@Builder
public class RoundContext {
	@Fact
	private Session.Move movePlayer1;

	@Fact
	private Session.Move movePlayer2;
}
