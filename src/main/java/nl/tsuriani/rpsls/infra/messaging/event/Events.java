package nl.tsuriani.rpsls.infra.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Events {
	@AllArgsConstructor
	@Getter
	public static class PlayerJoined extends Event {
		private String clientUUID;
		private String username;

		public PlayerJoined() {
			super(EventType.PLAYER_JOINED);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class PlayerRegisteredEvent extends Event {
		private String clientUUID;
		private String username;

		public PlayerRegisteredEvent() {
			super(EventType.PLAYER_REGISTERED);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class SessionIsCompleteEvent extends Event {
		private String sessionUUID;
		private String player1ClientUUID;
		private String player1Username;
		private String player2ClientUUID;
		private String player2Username;

		public SessionIsCompleteEvent() {
			super(EventType.SESSION_IS_COMPLETE);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class NewRoundIsStartedEvent extends Event {
		private String sessionUUID;
		private String player1ClientUUID;
		private String player1Username;
		private String player2ClientUUID;
		private String player2Username;

		public NewRoundIsStartedEvent() {
			super(EventType.NEW_ROUND_STARTED);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class PlayerChoseEvent extends Event {
		private String sessionUUID;
		private String clientUUID;
		private String username;
		private String move;

		public PlayerChoseEvent() {
			super(EventType.PLAYER_CHOSE);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class ScoreUpdatedEvent extends Event {
		private String sessionUUID;
		private String player1ClientUUID;
		private String player1Username;
		private int player1Score;
		private String player2ClientUUID;
		private String player2Username;
		private int player2Score;

		public ScoreUpdatedEvent() {
			super(EventType.SCORE_UPDATED);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class SessionIsTerminatedEvent extends Event {
		private String sessionUUID;
		private String player1ClientUUID;
		private String player1Username;
		private int player1Score;
		private String player2ClientUUID;
		private String player2Username;
		private int player2Score;

		public SessionIsTerminatedEvent() {
			super(EventType.SESSION_IS_TERMINATED);
		}
	}

	@AllArgsConstructor
	@Getter
	public static class SessionIsCancelledEvent extends Event {
		private String sessionUUID;
		private String player1ClientUUID;
		private String player1Username;
		private String player2ClientUUID;
		private String player2Username;

		public SessionIsCancelledEvent() {
			super(EventType.SESSION_IS_CANCELLED);
		}
	}
}
