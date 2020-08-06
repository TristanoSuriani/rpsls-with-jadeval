package nl.tsuriani.rpsls.infra.web.dto;

import lombok.Data;

@Data
public class ChoiceDTO {
	public String sessionUUID;
	public String clientUUID;
	public String username;
	public String move;
}
