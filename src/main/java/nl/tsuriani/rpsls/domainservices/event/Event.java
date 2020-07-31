package nl.tsuriani.rpsls.domainservices.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class Event {
	protected EventType type;
}
