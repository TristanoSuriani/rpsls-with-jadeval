package nl.tsuriani.rpsls.infra.di;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.suriani.jadeval.execution.decision.DecisionsDelegate;
import nl.tsuriani.rpsls.domain.round.RoundContext;

@AllArgsConstructor
@Getter
public class PostRoundDecisions {
	private DecisionsDelegate<RoundContext> decisionsDelegate;
}
