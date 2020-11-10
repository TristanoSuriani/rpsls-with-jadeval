package nl.tsuriani.rpsls.domain.session;

import lombok.Builder;
import lombok.Getter;
import nl.tsuriani.rpsls.domain.PlayerIdentity;

@Builder
@Getter
public class Player {
	private PlayerIdentity playerIdentity;
	private int score;
}
