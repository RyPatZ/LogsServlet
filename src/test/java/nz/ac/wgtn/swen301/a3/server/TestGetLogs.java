package nz.ac.wgtn.swen301.a3.server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * The type Test get logs.
 */
public class TestGetLogs {

    /**
     * Test returned values 1.
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues1() throws IOException, ParseException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "1");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");

        LogEvent logEvent = new LogEvent(object);

        LogsServlet service = new LogsServlet();
        Persistency persistency = new Persistency();
        persistency.addLogEvent(logEvent);
        service.setPersistency(persistency);

        service.doGet(request, response);

        String s = response.getContentAsString();
        String logs = s.substring(32);
        JSONArray jsonArray = new JSONArray(logs);
        JSONObject obj = jsonArray.getJSONObject(0);
        assert (logEvent.getId().equals(obj.get("id")));
        assert (logEvent.getMessage().equals(obj.get("message")));
        assert (logEvent.getLevel().toString().equals(obj.get("level")));
        assert (logEvent.getTimeString().equals(obj.get("timestamp")));
        assert (logEvent.getLogger().equals(obj.get("logger")));
        assert (logEvent.getErrorDetails().equals(obj.get("errorDetails")));
    }

    /**
     * Test returned values 2.
     * check if the system. Logs are returned ordered by timestamp
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues2() throws IOException, ParseException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "5");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

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
        object2.put("logger", "com.example.Foo");
        object2.put("level", "INFO");
        object2.put("errorDetails", "errorDetails");

        LogEvent logEvent = new LogEvent(object);
        LogEvent logEvent1 = new LogEvent(object1);
        LogEvent logEvent2 = new LogEvent(object2);

        LogsServlet service = new LogsServlet();
        Persistency persistency = new Persistency();

        persistency.addLogEvent(logEvent);
        persistency.addLogEvent(logEvent1);
        persistency.addLogEvent(logEvent2);


        service.setPersistency(persistency);
        service.doGet(request, response);

        String s = response.getContentAsString();
        String logs = s.substring(32);
        JSONArray jsonArray = new JSONArray(logs);

        JSONObject obj = jsonArray.getJSONObject(0);
        JSONObject obj1 = jsonArray.getJSONObject(1);
        JSONObject obj2 = jsonArray.getJSONObject(2);
        System.out.println(s);
        //check if order is correct
        assert (obj.get("id").equals("d290f1ee-6c54-4b01-90e6-d701748f0852"));
        assert (obj1.get("id").equals("d290f1ee-6c54-4b01-90e6-d701748f0851"));
        assert (obj2.get("id").equals("d290f1ee-6c54-4b01-90e6-d701748f0853"));
    }

    /**
     * Test returned values 3.
     * check if parameter level work , the level returned should be the same or higher than the minimum level
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues3() throws IOException, ParseException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "1");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "ALL");
        object.put("errorDetails", "errorDetails");

        JSONObject object2 = new JSONObject();

        object2.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object2.put("message", "application started");
        object2.put("timestamp", "04-05-2021 10:12:00");
        object2.put("thread", "main");
        object2.put("logger", "com.example.Foo");
        object2.put("level", "TRACE");
        object2.put("errorDetails", "errorDetails");

        LogsServlet service = new LogsServlet();

        LogEvent logEvent = new LogEvent(object);
        LogEvent logEvent2 = new LogEvent(object2);
        Persistency persistency = new Persistency();
        persistency.addLogEvent(logEvent);
        persistency.addLogEvent(logEvent2);


        service.setPersistency(persistency);
        service.doGet(request, response);
        String s = response.getContentAsString();
        String logs = s.substring(32);
        JSONArray jsonArray = new JSONArray(logs);
        assert (jsonArray.isEmpty());

        MockHttpServletRequest request1 = new MockHttpServletRequest();
        request1.setParameter("limit", "1");
        request1.setParameter("level", "DEBUG");
        MockHttpServletResponse response1 = new MockHttpServletResponse();

        JSONObject object1 = new JSONObject();

        object1.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object1.put("message", "application started");
        object1.put("timestamp", "04-05-2021 10:12:00");
        object1.put("thread", "main");
        object1.put("logger", "com.example.Foo");
        object1.put("level", "DEBUG");
        object1.put("errorDetails", "errorDetails");

        LogsServlet service1 = new LogsServlet();

        LogEvent logEvent1 = new LogEvent(object1);
        Persistency persistency1 = new Persistency();
        persistency1.addLogEvent(logEvent1);


        service1.setPersistency(persistency1);
        service1.doGet(request1, response1);
        String s1 = response1.getContentAsString();
        String logs1 = s1.substring(32);
        JSONArray jsonArray1 = new JSONArray(logs1);
        assert (!jsonArray1.getJSONObject(0).isEmpty());
    }

    /**
     * Test returned values 4.
     * check if parameter limit work , the number of logs returned should be higher than the limit
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues4() throws IOException, ParseException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "0");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");

        LogEvent logEvent = new LogEvent(object);
        Persistency persistency = new Persistency();
        persistency.addLogEvent(logEvent);

        LogsServlet service = new LogsServlet();
        service.setPersistency(persistency);
        service.doGet(request, response);
        String s = response.getContentAsString();
        String logs = s.substring(32);
        JSONArray jsonArray = new JSONArray(logs);
        assert (jsonArray.isEmpty());
    }


    /**
     * Test invalid request response code 1.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testInvalidRequestResponseCode1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // query parameter missing
        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    /**
     * Test invalid request response code 2.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testInvalidRequestResponseCode2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "5");
        request.setParameter("level", "non-valid-level");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    /**
     * Test invalid request response code 3.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testInvalidRequestResponseCode3() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    /**
     * Test invalid request response code 4.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testInvalidRequestResponseCode4() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "LEVEL");
        request.setParameter("level", "0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    /**
     * Test valid request response code.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testValidRequestResponseCode() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "5");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(200, response.getStatus());
    }

    /**
     * Test valid content type.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testValidContentType() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "5");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertTrue(response.getContentType().startsWith("application/json"));
    }


}
