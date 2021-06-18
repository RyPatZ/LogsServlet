package nz.ac.wgtn.swen301.a3.server;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * The type Test stats html.
 */
public class TestStatsHTML {


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
        object3.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0853");
        object3.put("message", "application started");
        object3.put("timestamp", "04-05-2021 05:50:00");
        object3.put("thread", "main");
        object3.put("logger", "com.example.Foo1");
        object3.put("level", "ERROR");
        object3.put("errorDetails", "errorDetails");

        LogEvent logEvent = new LogEvent(object);
        LogEvent logEvent1 = new LogEvent(object1);
        LogEvent logEvent2 = new LogEvent(object2);
        LogEvent logEvent3 = new LogEvent(object3);


        StatsServlet service = new StatsServlet();
        Persistency persistency = new Persistency();
        persistency.addLogEvent(logEvent);
        persistency.addLogEvent(logEvent1);
        persistency.addLogEvent(logEvent2);
        persistency.addLogEvent(logEvent3);

        service.setPersistency(persistency);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        service.doGet(request, response);
        String HTML = response.getContentAsString();
        Document doc = Jsoup.parse(HTML);
        String logsStr= doc.body().text();

        String[] HTMLres = logsStr.split("\\s+");

        assert (HTMLres.length==27);

        HashMap<String, Integer> Level_count = new HashMap<>();
        HashMap<String,HashMap<String, Integer>> Logger_Level = new HashMap<>();

        for (int i = 0; i < HTMLres.length; i+=9) {
            String Header = HTMLres[i];

            if (i == 0) {
                for (int j = 1; j < 9; j++) {
                    Level_count.put(HTMLres[i+j], 0);
                }
            }
            else {
                for (int j = 1; j < 9; j++) {
                    if(Integer.parseInt(HTMLres[i+j])!=0) {
                        if (Logger_Level.containsKey(Header)) {
                            Logger_Level.get(Header).replace(HTMLres[j], Integer.parseInt(HTMLres[i+j]));
                        } else {
                            Logger_Level.put(Header, new HashMap<>(Level_count));
                            Logger_Level.get(Header).replace(HTMLres[j], Integer.parseInt(HTMLres[i+j]));
                        }
                    }
                }
            }
        }

        for (
                String levels : Logger_Level.
                get("com.example.Foo").
                keySet()) {
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
                assert (Logger_Level.get("com.example.Foo1").get(levels) == 1);
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

        StatsServlet service = new StatsServlet();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        service.doGet(request, response);

        String HTML = response.getContentAsString();
        Document doc = Jsoup.parse(HTML);
        String logsStr= doc.body().text();

        String[] HTMLres = logsStr.split("\\s+");

        //no log event is stored so the row num should be 1 (means 9 elements in total)
        assert (HTMLres.length==9);
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

        StatsServlet service = new StatsServlet();
        service.doGet(request, response);

        assertTrue(response.getContentType().startsWith("text/html"));
    }
}
