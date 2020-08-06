package nl.tsuriani.rpsls.infra.di;

import nl.tsuriani.rpsls.applicationservices.RPSLSSessionService;
import nl.tsuriani.rpsls.domain.mutation.MutateSession;
import nl.tsuriani.rpsls.domain.rule.RPSLSRulesBuilder;
import nl.tsuriani.rpsls.domain.rule.Rules;
import nl.tsuriani.rpsls.domainservices.SessionService;
import nl.tsuriani.rpsls.infra.facade.SessionFacade;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class Configuration {
	@ApplicationScoped
	public Rules rules() {
		return new RPSLSRulesBuilder().build();
	}

	@ApplicationScoped
	public MutateSession mutateSession() {
		return new MutateSession(rules());
	}

	@ApplicationScoped
	public SessionService sessionService() {
		return new RPSLSSessionService(mutateSession());
	}

	@ApplicationScoped
	public SessionFacade sessionFacade() {
		System.out.println(UUID.randomUUID());
		return new SessionFacade(sessionService());
	}
}
