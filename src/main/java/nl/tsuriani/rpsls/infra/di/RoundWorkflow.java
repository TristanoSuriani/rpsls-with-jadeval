package nl.tsuriani.rpsls.infra.di;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.suriani.jadeval.execution.workflow.WorkflowDelegate;
import nl.tsuriani.rpsls.domain.round.RoundContext;

@AllArgsConstructor
@Getter
public class RoundWorkflow {
	private WorkflowDelegate<RoundContext> workflowDelegate;
}
