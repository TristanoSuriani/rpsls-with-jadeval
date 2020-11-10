package nl.tsuriani.rpsls.infra.db;

import lombok.extern.java.Log;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Log
public class NitriteConfiguration {
	@ConfigProperty(name = "rpsls.nitrite.filepath")
	String filePath;

	@ConfigProperty(name = "rpsls.nitrite.collection.sessions")
	String sessionsCollectionName;

	Nitrite db = nitriteDB();

	@ApplicationScoped
	public NitriteCollection getSessionsCollection() {
		return db.getCollection(sessionsCollectionName);
	}

	@PreDestroy
	public void closeNitriteDB() {
		log.info("Close database before destroying NitriteConfiguration");
		db.close();
	}

	private Nitrite nitriteDB() {
		return Nitrite.builder()
				.compressed()
				.filePath(filePath)
				.openOrCreate();
	}
}
