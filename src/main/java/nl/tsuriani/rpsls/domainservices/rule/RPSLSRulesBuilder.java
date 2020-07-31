package nl.tsuriani.rpsls.domainservices.rule;

import static nl.tsuriani.rpsls.domain.RPSLS.*;

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
