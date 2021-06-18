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
 * The type Test post logs.
 */
public class TestPostLogs {

    /**
     * Test returned values 1.
     *
     * @throws IOException    the io exception
     * @throws ParseException the parse exception
     */
    @Test
    public void testReturnedValues1() throws IOException, ParseException {
                MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");
        String s= object.toString();
        request.setContentType("application/json");
        request.setContent(s.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);


        MockHttpServletRequest request1 = new MockHttpServletRequest();
        request1.setParameter("limit", "1");
        request1.setParameter("level", "DEBUG");
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        service.doGet(request1,response1);
        String s1 = response1.getContentAsString();
        String logs = s1.substring(32);
        JSONArray jsonArray = new JSONArray(logs);
        JSONObject obj = jsonArray.getJSONObject(0);

        assert (obj.get("id").equals(object.get("id")));
        assert (obj.get("message").equals(object.get("message")));
        assert (obj.get("level").equals(object.get("level")));
        assert (obj.get("timestamp").equals(object.get("timestamp")));
        assert (obj.get("logger").equals(object.get("logger")));
        assert (obj.get("errorDetails").equals(object.get("errorDetails")));

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
        service.doPost(request, response);

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
        request.setParameter("LogEvents", "\"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
                "    \"message\": \"application started\",\n" +
                "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
                "    \"thread\": \"main\",\n" +
                "    \"logger\": \"com.example.Foo\",\n" +
                "    \"level\": \"DEBUG\",\n" +
                "    \"errorDetails\": \"string\"");
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

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
        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        String s= object.toString();
        request.setParameter("LogEvents", s);
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

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
        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");
        String s= object.toString();
        String s1= object.toString();
        request.setContentType("application/json");
        request.setContent(s.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();


        MockHttpServletRequest request1 = new MockHttpServletRequest();
        request1.setContentType("application/json");
        request1.setContent(s1.getBytes());
        MockHttpServletResponse response1 = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        service.doPost(request1,response1);

        assertEquals(409, response1.getStatus());
    }

    /**
     * Test valid request response code.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testValidRequestResponseCode() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0855");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");
        String s= object.toString();
        request.setContentType("application/json");
        request.setContent(s.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

        assertEquals(201, response.getStatus());
    }

    /**
     * Test valid content type.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testValidContentType() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject object = new JSONObject();

        object.put("id", "d290f1ee-6c54-4b01-90e6-d701748f0851");
        object.put("message", "application started");
        object.put("timestamp", "04-05-2021 10:12:00");
        object.put("thread", "main");
        object.put("logger", "com.example.Foo");
        object.put("level", "INFO");
        object.put("errorDetails", "errorDetails");
        String s= object.toString();
        request.setContentType("application/json");
        request.setContent(s.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        // wrong query parameter

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertTrue(response.getContentType().startsWith("application/json"));
    }
}
