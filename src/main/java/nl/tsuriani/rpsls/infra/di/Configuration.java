package nl.tsuriani.rpsls.infra.di;

import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.execution.workflow.WorkflowOptionsBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import java.io.File;

@ApplicationScoped
public class Configuration {
	private static final String JADEVAL_SOURCES_PATH = "../src/main/jadeval/nl/tsuriani/rpsls/domain/";
	private static final String ROUND_WORKFLOW_SOURCE_PATH = JADEVAL_SOURCES_PATH + "round_workflow.jwl";
	private static final String SESSION_WORKFLOW_SOURCE_PATH = JADEVAL_SOURCES_PATH + "session_workflow.jwl";
	private static final String ROUND_VALIDATIONS_SOURCE_PATH = JADEVAL_SOURCES_PATH + "round_validations.jvl";
	private static final String ROUND_RULES_SOURCE_PATH = JADEVAL_SOURCES_PATH + "round_rules.jdl";
	private static final String POST_ROUND_DECISIONS_SOURCE_PATH = JADEVAL_SOURCES_PATH + "post_round_decisions.jdl";

	@Singleton
	public RoundWorkflow roundWorkflow() {
		var model = new JadevalLoader().load(new File(ROUND_WORKFLOW_SOURCE_PATH));
		return new RoundWorkflow(new JadevalExecutor(model)
				.workflow(new WorkflowOptionsBuilder()
						.build()));
	}

	@Singleton
	public SessionWorkflow sessionWorkflow() {
		var model = new JadevalLoader().load(new File(SESSION_WORKFLOW_SOURCE_PATH));
		return new SessionWorkflow(new JadevalExecutor(model)
				.workflow(new WorkflowOptionsBuilder()
					.build()));
	}

	@Singleton
	public RoundValidations roundValidations() {
		var model = new JadevalLoader().load(new File(ROUND_VALIDATIONS_SOURCE_PATH));
		return new RoundValidations(new JadevalExecutor(model)
				.validation());
	}

	@Singleton
	public RoundRules roundRules() {
		var model = new JadevalLoader().load(new File(ROUND_RULES_SOURCE_PATH));
		return new RoundRules(new JadevalExecutor(model)
				.decision());
	}

	@Singleton
	public PostRoundDecisions postRoundDecisions() {
		var model = new JadevalLoader().load(new File(POST_ROUND_DECISIONS_SOURCE_PATH));
		return new PostRoundDecisions(new JadevalExecutor(model)
				.decision());
	}
}
