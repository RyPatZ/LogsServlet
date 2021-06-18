package nz.ac.wgtn.swen301.a3.server;


import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import java.io.IOException;
import java.text.ParseException;
import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import static org.junit.Assert.assertTrue;


/**
 * The type Test stats xls.
 */
public class TestStatsXLS {

    /**
     * Test returned values 1.
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues1() throws IOException, ParseException {
        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");
        JSONObject object1 = new JSONObject();

        object1.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0852");
        object1.put("message", "application started");
        object1.put("timestamp", "10-05-2021 12:12:00");
        object1.put("thread", "main");
        object1.put("logger", "com.example.Foo");
        object1.put("level", "INFO");
        object1.put("errorDetails", "errorDetails");

        JSONObject object2 = new JSONObject();

        object2.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0853");
        object2.put("message", "application started");
        object2.put("timestamp", "04-05-2021 05:50:00");
        object2.put("thread", "main");
        object2.put("logger", "com.example.Foo1");
        object2.put("level", "DEBUG");
        object2.put("errorDetails", "errorDetails");

        JSONObject object3 = new JSONObject();
        object3.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0854");
        object3.put("message", "application started");
        object3.put("timestamp", "04-05-2021 05:50:00");
        object3.put("thread", "main");
        object3.put("logger", "com.example.Foo1");
        object3.put("level", "ERROR");
        object3.put("errorDetails", "errorDetails");

        JSONObject object4 = new JSONObject();
        object4.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0855");
        object4.put("message", "application started");
        object4.put("timestamp", "04-05-2021 05:50:00");
        object4.put("thread", "main");
        object4.put("logger", "com.example.Foo1");
        object4.put("level", "ERROR");
        object4.put("errorDetails", "errorDetails");

        LogEvent logEvent = new LogEvent(object);
        LogEvent logEvent1 = new LogEvent(object1);
        LogEvent logEvent2 = new LogEvent(object2);
        LogEvent logEvent3 = new LogEvent(object3);
        LogEvent logEvent4 = new LogEvent(object4);

        StatsXLSServlet service = new StatsXLSServlet();
        Persistency persistency = new Persistency();
        persistency.addLogEvent(logEvent);
        persistency.addLogEvent(logEvent1);
        persistency.addLogEvent(logEvent2);
        persistency.addLogEvent(logEvent3);
        persistency.addLogEvent(logEvent4);

        service.setPersistency(persistency);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        service.doGet(request, response);

        byte[] responseContent = response.getContentAsByteArray();
        Workbook workbook = new HSSFWorkbook(
                new ByteArrayInputStream(responseContent));
        Sheet sheet = workbook.getSheet("statsXLS");

        HashMap<String, Integer> Level_count = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> Logger_Level = new HashMap<>();

        int i = 0;
        Row firstRow = sheet.getRow(0);
        for (Row row : sheet)     //iteration over row using for each loop
        {
            if (i == 0) {
                for (int j = 1; j < 9; j++) {
                    Level_count.put(row.getCell(j).getStringCellValue(), 0);
                }
            } else {
                int j = 0;
                for (Cell cell : row)    //iteration over cell using for each loop
                {
                    String Header = row.getCell(0).getStringCellValue();
                    if (j != 0) {
                        if (cell.getNumericCellValue() != 0) {
                            if (Logger_Level.containsKey(Header)) {
                                Logger_Level.get(Header).replace(firstRow.getCell(j).getStringCellValue(), (int) cell.getNumericCellValue());
                            } else {
                                Logger_Level.put(Header, new HashMap<>(Level_count));
                                Logger_Level.get(Header).replace(firstRow.getCell(j).getStringCellValue(), (int) cell.getNumericCellValue());
                            }
                        }
                    }
                    j++;
                }
            }
            i++;
        }

        //three logger in total so row num should be 3
        assert (i==3);

        for (
                String levels : Logger_Level.
                get("com.example.Foo").
                keySet()) {
            System.out.println(Logger_Level.get("com.example.Foo1").get(levels));
            System.out.println(levels);
            if (levels.equals("INFO")) {
                assert (Logger_Level.get("com.example.Foo").get(levels) == 2);
            } else {
                assert (Logger_Level.get("com.example.Foo").get(levels) == 0);
            }
        }
        for (
                String levels : Logger_Level.
                get("com.example.Foo1").
                keySet()) {
            if (levels.equals("DEBUG")) {
                assert (Logger_Level.get("com.example.Foo1").get(levels) == 1);
            }
            else if (levels.equals("ERROR")){
                assert (Logger_Level.get("com.example.Foo1").get(levels) == 2);
            }
            else {
                assert (Logger_Level.get("com.example.Foo1").get(levels) == 0);
            }
        }
    }

    /**
     * Test returned values 2.
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues2() throws IOException, ParseException {

        StatsXLSServlet service = new StatsXLSServlet();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        service.doGet(request, response);

        byte[] responseContent = response.getContentAsByteArray();
        Workbook workbook = new HSSFWorkbook(
                new ByteArrayInputStream(responseContent));
        Sheet sheet = workbook.getSheet("statsXLS");

        int i =0;
        for(Row row: sheet){
            i++;
        }
        //no log event is stored so the row num should be 1
        assert (i==1);
    }

    /**
     * Test valid content type.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testValidContentType() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        StatsCSVServlet service = new StatsCSVServlet();
        service.doGet(request, response);

        assertTrue(response.getContentType().startsWith("text/csv"));
    }
}
