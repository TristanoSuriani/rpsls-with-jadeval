package nl.tsuriani.rpsls.domain.rule;

import static nl.tsuriani.rpsls.domain.RPSLS.lizard;
import static nl.tsuriani.rpsls.domain.RPSLS.paper;
import static nl.tsuriani.rpsls.domain.RPSLS.rock;
import static nl.tsuriani.rpsls.domain.RPSLS.scissors;
import static nl.tsuriani.rpsls.domain.RPSLS.spock;

public class RPSLSRulesBuilder extends RulesBuilder {
	@Override
	protected void compile() {
		setMaximumScore(3);

		rule().move(rock())
				.beats(scissors())
				.and(lizard())
				.end();

		rule().move(paper())
				.beats(rock())
				.and(spock())
				.end();

		rule().move(scissors())
				.beats(paper())
				.and(lizard())
				.end();

		rule().move(lizard())
				.beats(paper())
				.and(spock())
				.end();

		rule().move(spock())
				.beats(scissors())
				.and(rock())
				.end();
	}
}
