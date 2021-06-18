package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * The type Stats csv servlet.
 */
public class StatsCSVServlet extends HttpServlet {

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
    HashMap<String,HashMap<String, Integer>> Logger_Level = new HashMap<>();

    /**
     * Instantiates a new Stats csv servlet.
     */
    public StatsCSVServlet() {
        this.persistency = new Persistency();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        List<LogEvent> DBlogging = persistency.getDB();

        response.setStatus(200);

        List<LogEvent> Logs = new ArrayList<>();

        for (LogEvent log : DBlogging) {
            Logs.add(log);
        }

        StringBuilder sb = new StringBuilder("logger"+"\t");
        HashMap<String, Integer> Level_count = new HashMap<>();
        for (int i =0; i<levels.size();i++) {
            if(i==levels.size()-1){
                sb.append(levels.get(i));
            }else{
                sb.append(levels.get(i) + "\t");
            }
            Level_count.put(levels.get(i),0);
        }
        sb.append("\n");


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
            sb.append(key+"\t");
            sb.append(Logger_Level.get(key).get("ALL")+"\t");
            sb.append(Logger_Level.get(key).get("TRACE")+"\t");
            sb.append(Logger_Level.get(key).get("DEBUG")+"\t");
            sb.append(Logger_Level.get(key).get("INFO")+"\t");
            sb.append(Logger_Level.get(key).get("WARN")+"\t");
            sb.append(Logger_Level.get(key).get("ERROR")+"\t");
            sb.append(Logger_Level.get(key).get("FATAL")+"\t");
            sb.append(Logger_Level.get(key).get("OFF")+"\n");
        }

        String output = sb.toString();

        String filename = null;
        try {
            filename = request.getParameter("filename");
        }
        catch (Exception e){
        }
        if(request.getParameter("filename")==null){
            filename = "statsCSVServlet.csv";
        }

        FileOutputStream outputStreamfile = new FileOutputStream(filename);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(output.getBytes());
        outputStreamfile.write(output.getBytes());
        outputStream.flush();
        outputStreamfile.flush();
        outputStream.close();
        outputStreamfile.close();
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
