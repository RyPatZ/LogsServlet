package nz.ac.wgtn.swen301.a3.server;

import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Log event.
 */
class LogEvent  {


    /**
     * The enum Level.
     */
    enum Level {
        /**
         * All level.
         */
        ALL,
        /**
         * Trace level.
         */
        TRACE,
        /**
         * Debug level.
         */
        DEBUG,
        /**
         * Info level.
         */
        INFO,
        /**
         * Warn level.
         */
        WARN,
        /**
         * Error level.
         */
        ERROR,
        /**
         * Fatal level.
         */
        FATAL,
        /**
         * Off level.
         */
        OFF;}


    private String id;
    private String message;
    private Date timestamp;
    private String timeString;
    private String thread;
    private String logger;
    private Level level;
    private String errorDetails;

    /**
     * Instantiates a new Log event.
     *
     * @param logEventJson the log event json
     * @throws JSONException  the json exception
     * @throws ParseException the parse exception
     */
    LogEvent(JSONObject logEventJson) throws JSONException, ParseException {


        this.id = logEventJson.getString("id");
        this.message = logEventJson.getString("message");

        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(logEventJson.getString("timestamp"));
        this.timeString= logEventJson.getString("timestamp");


        this.thread = logEventJson.getString("thread");
        this.logger = logEventJson.getString("logger");


        this.level = Level.valueOf(logEventJson.getString("level"));


        this.errorDetails = logEventJson.getString("errorDetails");

    }

    /**
     * Compare level boolean.
     *
     * @param logLevel the log level
     * @return the boolean
     */
    public boolean compareLevel(LogEvent.Level logLevel) {
        return this.level.compareTo(logLevel) >= 0;
    }

    /**
     * Format string.
     *
     * @return the string
     */
    public String format() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("message", message);
        object.put("timestamp", timeString);
        object.put("thread", thread);
        object.put("logger", logger);
        object.put("level", level.toString());
        object.put("errorDetails", errorDetails);

        StringBuffer sb = new StringBuffer("{\n");
        sb.append("\t\t").append("\"").append("id").append("\":\"")
                .append(object.get("id")).append("\",\n");
        sb.append("\t\t").append("\"").append("message").append("\":\"")
                .append(object.get("message")).append("\",\n");
        sb.append("\t\t").append("\"").append("timestamp").append("\":\"")
                .append(object.get("timestamp")).append("\",\n");
        sb.append("\t\t").append("\"").append("logger").append("\":\"")
                .append(object.get("logger")).append("\",\n");
        sb.append("\t\t").append("\"").append("level").append("\":\"")
                .append(object.get("level")).append("\",\n");
        sb.append("\t\t").append("\"").append("errorDetails").append("\":\"")
                .append(object.get("errorDetails")).append("\",\n");
        sb.append("\t}");

        return sb.toString();
    }


    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Gets time string.
     *
     * @return the time string
     */
    public String getTimeString() {
        return timeString;
    }

    /**
     * Gets thread.
     *
     * @return the thread
     */
    public String getThread() {
        return thread;
    }

    /**
     * Gets logger.
     *
     * @return the logger
     */
    public String getLogger() {
        return logger;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets error details.
     *
     * @return the error details
     */
    public String getErrorDetails() {
        return errorDetails;
    }
}
