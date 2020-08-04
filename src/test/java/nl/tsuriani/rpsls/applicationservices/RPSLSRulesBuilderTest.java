package nl.tsuriani.rpsls.applicationservices;

import nl.tsuriani.rpsls.domain.rule.RPSLSRulesBuilder;
import nl.tsuriani.rpsls.domain.rule.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.tsuriani.rpsls.domain.RPSLS.lizard;
import static nl.tsuriani.rpsls.domain.RPSLS.paper;
import static nl.tsuriani.rpsls.domain.RPSLS.rock;
import static nl.tsuriani.rpsls.domain.RPSLS.scissors;
import static nl.tsuriani.rpsls.domain.RPSLS.spock;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RPSLSRulesBuilderTest {
	private Rules rules;

	@BeforeEach
	void setup() {
		rules = new RPSLSRulesBuilder().build();
	}

	@Test
	void sameMovesDraw() {
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(rock(), rock()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(paper(), paper()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(scissors(), scissors()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(lizard(), lizard()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(spock(), spock()));

		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(rock(), rock()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(paper(), paper()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(scissors(), scissors()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(lizard(), lizard()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(spock(), spock()));
	}

	@Test
	void rockWinsAgainstScissorsAndLizard() {
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(rock(), scissors()));
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(rock(), lizard()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(rock(), lizard()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(rock(), lizard()));

		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(scissors(), rock()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(lizard(), rock()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(scissors(), rock()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(lizard(), rock()));
	}

	@Test
	void paperWinsAgainstRockAndSpock() {
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(paper(), rock()));
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(paper(), spock()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(paper(), rock()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(paper(), spock()));

		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(rock(), paper()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(spock(), paper()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(rock(), paper()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(spock(), paper()));
	}

	@Test
	void scissorsWinsAgainstPaperAndLizard() {
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(scissors(), paper()));
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(scissors(), lizard()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(scissors(), paper()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(scissors(), lizard()));

		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(paper(), scissors()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(lizard(), scissors()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(paper(), scissors()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(lizard(), scissors()));
	}

	@Test
	void lizardWinsAgainstPaperAndSpock() {
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(lizard(), paper()));
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(lizard(), spock()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(lizard(), paper()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(lizard(), spock()));

		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(paper(), lizard()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(spock(), lizard()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(paper(), lizard()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(spock(), lizard()));
	}

	@Test
	void spockWinsAgainstRockAndScissors() {
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(spock(), rock()));
		assertTrue(rules.firstMoveScoresAgainstTheSecondOne(spock(), scissors()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(spock(), rock()));
		assertFalse(rules.secondMoveScoresAgainstTheFirstOne(spock(), scissors()));

		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(rock(), spock()));
		assertFalse(rules.firstMoveScoresAgainstTheSecondOne(scissors(), spock()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(rock(), spock()));
		assertTrue(rules.secondMoveScoresAgainstTheFirstOne(scissors(), spock()));
	}
}
