package nl.tsuriani.rpsls.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder
@EqualsAndHashCode
@Getter
public class SessionIdentity {
	private UUID sessionUUID;
}
