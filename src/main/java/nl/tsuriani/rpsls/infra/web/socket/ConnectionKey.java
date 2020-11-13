package nl.tsuriani.rpsls.infra.web.socket;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConnectionKey {
	private String sessionUUID;
	private String clientUUID;
	private String username;
}
