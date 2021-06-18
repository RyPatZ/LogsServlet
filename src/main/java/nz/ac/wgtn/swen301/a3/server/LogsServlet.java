package nz.ac.wgtn.swen301.a3.server;


import org.json.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


/**
 * The type Logs servlet.
 */
public class LogsServlet extends HttpServlet {

    /**
     * The Persistency.
     */
    private Persistency persistency;


    /**
     * Instantiates a new Logs servlet.
     */
    public LogsServlet() {
        this.persistency = new Persistency();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        List<LogEvent> DBlogging = persistency.getDB();
        LogEvent.Level minLevel = null;
        int maxLimit = 0;


        try {
            String searchLogsLimit = request.getParameter("limit");
            String searchLogsLevel = request.getParameter("level");
            minLevel = LogEvent.Level.valueOf(searchLogsLevel);
            maxLimit = Integer.parseInt(searchLogsLimit);
            PrintWriter out = response.getWriter();
        } catch (Exception e) {
            response.setStatus(400);
            response.sendError(400);
            PrintWriter out = response.getWriter();
            out.write("bad or missing input parameter (for instance, level is not a valid level or limit is not a positive, non-zero integer)");
            out.close();
            return;
        }
        response.setStatus(200);

        List<LogEvent> Logs = new ArrayList<>();

        Collections.sort(DBlogging, new Comparator<LogEvent>() {
            public int compare(LogEvent o1, LogEvent o2) {
                if (o1.getTimestamp() == null || o2.getTimestamp() == null) {
                    return 0;
                }
                if (o1.getTimestamp().compareTo(o2.getTimestamp()) == 1) {
                    return -1;
                } else if (o1.getTimestamp().compareTo(o2.getTimestamp()) == -1) {
                    return 1;
                }
                return 0;
            }
        });

        for (LogEvent log : DBlogging) {
            if (log.compareLevel(minLevel) && Logs.size() < maxLimit) {
                Logs.add(log);
            }
        }

        StringBuilder l = new StringBuilder("[");

        for (LogEvent log : Logs) {
            String currentLog = log.format();
            l.append("\n\t").append(currentLog).append(",");
        }
        l = new StringBuilder(l.toString().substring(0, l.length()) + "\n]");
        String logs = l.toString();

        PrintWriter out = response.getWriter();
        out.write("search results matching criteria\n");
        out.write(logs);
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        if(request.getContentType()==null){
            response.setStatus(400);
            out.write("invalid input, object invalid");
            return;
        }

        BufferedReader bf = request.getReader();
        String PostLogEvenets= bf.readLine();
        if(PostLogEvenets==null){
            response.setStatus(400);
            out.write("invalid input, object invalid");
            return;
        }

        response.setContentType("application/json");
        JSONObject jsonObj = null;
        LogEvent logEvent = null;
        try {
            jsonObj = new JSONObject(PostLogEvenets);
            logEvent = new LogEvent(jsonObj);
        } catch (Exception e) {
            response.setStatus(400);
            response.sendError(400);
            out.write("invalid input, object invalid");
            out.close();
            return;
        }


        for (LogEvent le : persistency.getDB()) {
            if (le.getId().equals(logEvent.getId())) {
                response.setStatus(409);
                response.sendError(409);
                out.write("a log event with this id aleady exists");
                out.close();
                return;

            }
        }


        persistency.addLogEvent(logEvent);
        response.setStatus(201);
        out.write("item created");
        out.close();


    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        persistency.deleteAll();
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.write("all logs have been deleted");
        out.close();
    }

    /**
     * Sets persistency.
     *
     * @param persistency the persistency
     */
    public void setPersistency(Persistency persistency) {
        this.persistency = persistency;
    }

    /**
     * Gets persistency.
     *
     * @return the persistency
     */
    public Persistency getPersistency() {
        return persistency;
    }
}
