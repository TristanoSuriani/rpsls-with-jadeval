package nl.tsuriani.rpsls.infra.di;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.suriani.jadeval.execution.validation.ValidationsDelegate;
import nl.tsuriani.rpsls.domain.round.RoundContext;

@AllArgsConstructor
@Getter
public class RoundValidations {
	private ValidationsDelegate<RoundContext> decisionsDelegate;
}
