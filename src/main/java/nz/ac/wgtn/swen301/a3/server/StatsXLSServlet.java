package nz.ac.wgtn.swen301.a3.server;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The type Stats servlet.
 */
public class StatsXLSServlet extends HttpServlet {
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
    public StatsXLSServlet() {
        this.persistency = new Persistency();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        List<LogEvent> DBlogging = persistency.getDB();

        Workbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("statsXLS");


        response.setStatus(200);

        List<LogEvent> Logs = new ArrayList<>();

        for (LogEvent log : DBlogging) {
            Logs.add(log);
        }



        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        Cell cell = row.createCell(colNum++);
        cell.setCellValue((String) "logger");
        HashMap<String, Integer> Level_count = new HashMap<>();
        for (int i =0; i<levels.size();i++) {
            Cell cell1 = row.createCell(colNum++);
            cell1.setCellValue((String) levels.get(i));
            Level_count.put(levels.get(i),0);
        }
        for (LogEvent log : Logs) {
            String Logger= log.getLogger();
            LogEvent.Level l = log.getLevel();
            String level = String.valueOf(l);
            if(Logger_Level.containsKey(Logger)){
                Logger_Level.get(Logger).replace(level,Logger_Level.get(Logger).get(level)+1);
                System.out.println(Logger_Level.get(Logger).get(level));
                System.out.println(Logger);
            }
            else {
                Logger_Level.put(Logger,new HashMap<>(Level_count));
                Logger_Level.get(Logger).replace(level,Logger_Level.get(Logger).get(level)+1);
            }
        }

        for(String key:Logger_Level.keySet()){
            Row row1 = sheet.createRow(rowNum++);
            int colNum1 = 0;
            Cell cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((String) key);
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("ALL"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("TRACE"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("DEBUG"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("INFO"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("WARN"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("ERROR"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("FATAL"));
            cell2 = row1.createCell(colNum1++);
            cell2.setCellValue((Integer) Logger_Level.get(key).get("OFF"));
        }

        String filename = null;
        try {
            filename = request.getParameter("filename");
        }
        catch (Exception e){
        }
        if(request.getParameter("filename")==null){
            filename = "statsXLSServlet.xls";
        }

        FileOutputStream outputStream = new FileOutputStream(filename);
        workbook.write(outputStream);
        OutputStream out = response.getOutputStream();
        workbook.write(out);

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
