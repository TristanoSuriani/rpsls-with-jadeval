package nl.tsuriani.rpsls.infra.di;

import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.execution.decision.DecisionsDelegate;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.suriani.jadeval.execution.workflow.WorkflowExecutionType;
import nl.suriani.jadeval.execution.workflow.WorkflowOptions;
import nl.suriani.jadeval.execution.workflow.WorkflowOptionsBuilder;
import nl.suriani.jadeval.models.JadevalModel;
import nl.tsuriani.rpsls.infra.service.RPSLSSessionService;
import nl.tsuriani.rpsls.infra.service.context.RoundContext;
import nl.tsuriani.rpsls.infra.service.context.SessionContext;
import nl.tsuriani.rpsls.infra.service.context.SessionContextFactory;
import nl.tsuriani.rpsls.infra.service.interceptors.AuditTransitionAttempted;
import nl.tsuriani.rpsls.infra.service.interceptors.EvaluateRoundOnStateUpdateTransformer;
import nl.tsuriani.rpsls.infra.service.SessionService;
import nl.tsuriani.rpsls.infra.service.SessionFacade;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class Configuration {

	@ApplicationScoped
	public DecisionsDelegate<RoundContext> decisions() {
		return new JadevalExecutor(getDecisionsModel()).decision();
	}

	@ApplicationScoped
	public WorkflowDelegate<SessionContext> workflow() {
		return new JadevalExecutor(getWorkflowModel()).workflow(getWorkflowOptions());
	}

	@ApplicationScoped
	public SessionContextFactory sessionContextFactory() {
		return new SessionContextFactory();
	}

	@ApplicationScoped
	public SessionService sessionService() {
		return new RPSLSSessionService(workflow(), sessionContextFactory());
	}

	@ApplicationScoped
	public SessionFacade sessionFacade() {
		System.out.println(UUID.randomUUID());
		return new SessionFacade(sessionService());
	}

	private AuditTransitionAttempted auditTransitionAttempted() {
		return new AuditTransitionAttempted();
	}

	private JadevalModel getWorkflowModel() {
		return new JadevalLoader().load(this.getClass().getClassLoader().getResourceAsStream("game_workflow.jwl"));
	}

	private JadevalModel getDecisionsModel() {
		return new JadevalLoader().load(this.getClass().getClassLoader().getResourceAsStream("round_rules.jdl"));
	}

	private WorkflowOptions<SessionContext> getWorkflowOptions() {
		return new WorkflowOptionsBuilder<SessionContext>()
				.withExecutionType(WorkflowExecutionType.UNTIL_PAUSE)
				.withTransitionAttemptedEventHandler(auditTransitionAttempted())
				.withOnStateUpdateContextTransformer(new EvaluateRoundOnStateUpdateTransformer(decisions()))
				.build();
	}
}
