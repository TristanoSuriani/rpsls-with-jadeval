package nl.tsuriani.rpsls.domainservices.rule;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nl.tsuriani.rpsls.domain.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public abstract class RulesBuilder {
	private Map<Session.Move, Set<Session.Move>> rules;
	private int maximumScore;

	private final int DEFAULT_MAXIMUM_SCORE = 2;

	protected abstract void compile();

	protected void setMaximumScore(int maximumScore) {
		this.maximumScore = maximumScore;
	}

	public Rules build() {
		init();
		compile();
		return new Rules(rules, maximumScore);
	}

	protected Rule rule() {
		return new Rule();
	}

	protected class Rule {
		public BMove move(Session.Move beatingMove) {
			return new BMove(beatingMove);
		}
	}

	@AllArgsConstructor
	protected class BMove {
		private Session.Move beatingMove;

		public Beats beats(Session.Move beatenMove) {
			HashSet<Session.Move> beatenMoves = new HashSet<>();
			beatenMoves.add(beatenMove);
			return new Beats(beatingMove, beatenMoves);
		}
	}

	@AllArgsConstructor
	protected class Beats {
		private Session.Move beatingMove;
		private HashSet<Session.Move> beatenMoves;

		public Beats and(Session.Move beatenMove) {
			HashSet<Session.Move> beatenMoves = new HashSet<>(this.beatenMoves);
			beatenMoves.add(beatenMove);
			return new Beats(beatingMove, beatenMoves);
		}

		public void end() {
			rules.put(beatingMove, beatenMoves);
		}
	}

	private void init() {
		rules = new HashMap<>();
		maximumScore = DEFAULT_MAXIMUM_SCORE;
	}
}
