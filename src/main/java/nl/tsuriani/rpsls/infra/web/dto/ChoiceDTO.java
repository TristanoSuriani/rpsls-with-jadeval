package nl.tsuriani.rpsls.infra.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDTO {
	public String sessionUUID;
	public String clientUUID;
	public String username;
	public String move;
}
