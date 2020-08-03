package nl.tsuriani.rpsls.domain.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tsuriani.rpsls.domain.Session;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class Rules {
	private Map<Session.Move, Set<Session.Move>> rules;

	@Getter
	private int maximumScore;

	public boolean firstMoveScoresAgainstTheSecondOne(Session.Move move1, Session.Move move2) {
		if (!rules.containsKey(move1)) {
			return false;
		} else {
			return rules.get(move1).contains(move2);
		}
	}

	public boolean secondMoveScoresAgainstTheFirstOne(Session.Move move1, Session.Move move2) {
		return firstMoveScoresAgainstTheSecondOne(move2, move1);
	}
}
