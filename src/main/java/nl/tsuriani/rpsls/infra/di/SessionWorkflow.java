package nl.tsuriani.rpsls.infra.di;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.tsuriani.rpsls.domain.session.SessionContext;

@AllArgsConstructor
@Getter
public class SessionWorkflow {
	private WorkflowDelegate<SessionContext> workflowDelegate;
}
