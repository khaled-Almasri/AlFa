package de.unibremen.cs.swp.bokerfi.service;

import de.unibremen.cs.swp.bokerfi.model.Asset;
import de.unibremen.cs.swp.bokerfi.model.AssetHistory;
import de.unibremen.cs.swp.bokerfi.model.Person;
import de.unibremen.cs.swp.bokerfi.persistence.AssetHistoryRepository;
import de.unibremen.cs.swp.bokerfi.persistence.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service zum Protokollieren von Asset-Ereignissen.
 */
@Service
@Transactional
public class HistoryService {

    private final AssetHistoryRepository historyRepository;
    private final PersonRepository personRepository;

    public HistoryService(AssetHistoryRepository historyRepository, PersonRepository personRepository) {
        this.historyRepository = historyRepository;
        this.personRepository = personRepository;
    }

    /**
     * Protokolliert ein Ereignis.
     *
     * @param asset       Das betroffene Asset.
     * @param eventType   Art des Ereignisses (z.B. "ASSIGNED", "RETURNED").
     * @param description Optionale Beschreibung.
     * @param actorUuid   UUID des Akteurs (optional, null für System).
     */
    public void logEvent(Asset asset, String eventType, String description, Long actorUuid) {
        Person actor = null;
        if (actorUuid != null) {
            actor = personRepository.findById(actorUuid).orElse(null);
            // If actor not found by ID (which is internal ID, not UUID usually in args?), 
            // NOTE: Arguments in controllers are usually UUIDs, but internal logic often uses entities.
            // Let's adjust signature to take Person entity or String username if easier, 
            // but usually we have the current user available in Controller.
        }
        createAndSave(asset, eventType, description, actor);
    }
    
    public void logEvent(Asset asset, String eventType, String description, Person actor) {
        createAndSave(asset, eventType, description, actor);
    }

    public void logEvent(Asset asset, String eventType, String description, String actorEmail) {
        Person actor = null;
        if (actorEmail != null) {
             actor = personRepository.findByEmail(actorEmail).orElse(null);
        }
        createAndSave(asset, eventType, description, actor);
    }

    private void createAndSave(Asset asset, String eventType, String description, Person actor) {
        AssetHistory history = new AssetHistory();
        history.setAsset(asset);
        history.setEventType(eventType);
        history.setDescription(description);
        history.setActor(actor);
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }
}
