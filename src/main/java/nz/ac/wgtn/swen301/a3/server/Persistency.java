package nz.ac.wgtn.swen301.a3.server;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Persistency.
 */
public class Persistency {
    /**
     * The Db.
     */
    public static List<LogEvent> DB ;

    /**
     * Instantiates a new Persistency.
     */
    public Persistency(){
        this.DB = new ArrayList<>();
    }


    /**
     * Add log event.
     *
     * @param logEvent the log event
     */
    public void addLogEvent(LogEvent logEvent) {
        this.DB.add(logEvent);
    }

    /**
     * Delete all.
     */
    public void deleteAll(){
        this.getDB().clear();
    }

    /**
     * Gets db.
     *
     * @return the db
     */
    public List<LogEvent> getDB() {
        return DB;
    }


}
