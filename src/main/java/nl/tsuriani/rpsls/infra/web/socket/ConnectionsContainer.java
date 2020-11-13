package nl.tsuriani.rpsls.infra.web.socket;

import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.SendResult;
import javax.websocket.Session;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@Log
public class ConnectionsContainer {
    private Map<ConnectionKey, Session> connections =  new ConcurrentHashMap<>();

    public Session get(ConnectionKey connectionKey) {
    	return connections.get(connectionKey);
	}

	public Optional<ConnectionKey> iFeelLucky(String clientUUID, String username) {
    	return connections.entrySet().stream()
				.filter(entry -> entry.getKey().getClientUUID().equals(clientUUID))
				.filter(entry -> entry.getKey().getUsername().equals(username))
				.map(Map.Entry::getKey)
				.findFirst();
	}

	public Optional<ConnectionKey> findOpponent(ConnectionKey connectionKey) {
		return connections.entrySet().stream()
				.filter(entry -> entry.getKey().getSessionUUID().equals(connectionKey.getSessionUUID()))
				.filter(entry -> !entry.getKey().getClientUUID().equals(connectionKey.getClientUUID()))
				.filter(entry -> !entry.getKey().getUsername().equals(connectionKey.getUsername()))
				.map(Map.Entry::getKey)
				.findFirst();
	}

    public void put(ConnectionKey connectionKey, Session session) {
    	connections.put(connectionKey, session);
	}

	public void remove(ConnectionKey connectionKey) {
    	connections.remove(connectionKey);
	}

	public void notifyConnection(ConnectionKey connectionKey, Object message) {
    	var connection = connections.get(connectionKey);
    	if (connection != null) {
    		connection.getAsyncRemote().sendObject(message, this::handleSendResult);
		}
	}

	private void handleSendResult(SendResult sendResult) {
    	if (!sendResult.isOK()) {
    		log.severe(sendResult.getException().getLocalizedMessage());
		}
	}
}
