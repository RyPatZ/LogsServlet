package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

/**
 * The type Stats servlet.
 */
public class StatsServlet extends HttpServlet {
    /**
     * The Persistency.
     */
    Persistency persistency;


    /**
     * The Levels.
     */
    ArrayList<String> levels = new ArrayList<>( Arrays.asList("ALL",  "TRACE","DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"));

    /**
     * The Logger level.
     */
    HashMap<String, HashMap<String, Integer>> Logger_Level = new HashMap<>();

    /**
     * Instantiates a new Stats servlet.
     */
    public StatsServlet() {
        this.persistency = new Persistency();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        List<LogEvent> DBlogging = persistency.getDB();

        response.setStatus(200);

        List<LogEvent> Logs = new ArrayList<>();

        for (LogEvent log : DBlogging) {
            Logs.add(log);
        }
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<table border=1>" );
        out.println("<tr>");
        out.println("<th>"+"logger"+"</th>");
        HashMap<String, Integer> Level_count = new HashMap<>();
        for (int i =0; i<levels.size();i++) {
            out.println("<th>"+levels.get(i)+"</th>");
            Level_count.put(levels.get(i),0);
        }
        out.println("</tr>");

        for (LogEvent log : Logs) {
            String Logger= log.getLogger();
            LogEvent.Level l = log.getLevel();
            String level = String.valueOf(l);
            if(Logger_Level.containsKey(Logger)){
                Logger_Level.get(Logger).replace(level,Logger_Level.get(Logger).get(level)+1);
            }
            else {
                Logger_Level.put(Logger,new HashMap<>(Level_count));
                Logger_Level.get(Logger).replace(level,Logger_Level.get(Logger).get(level)+1);
            }
        }

        for(String key:Logger_Level.keySet()){
            out.println("<tr>");
            out.println("<td>"+key+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("ALL")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("TRACE")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("DEBUG")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("INFO")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("WARN")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("ERROR")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("FATAL")+"</th>");
            out.println("<td>"+Logger_Level.get(key).get("OFF")+"</th>");
            out.println("</tr>");
        }

        out.println("</table></body></html>");
        out.close();


        response.setStatus(200);
    }

    /**
     * Sets persistency.
     *
     * @param persistency the persistency
     */
    public void setPersistency(Persistency persistency) {
        this.persistency = persistency;
    }

   }
