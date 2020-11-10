package nl.tsuriani.rpsls.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder
@EqualsAndHashCode
@Getter
public class PlayerIdentity {
	private UUID clientUUID;
	private String username;
}
