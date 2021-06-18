package nz.ac.wgtn.swen301.a3.server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * The type Test delete logs.
 */
public class TestDeleteLogs {

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
        // wrong query parameter

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

        MockHttpServletRequest requestd = new MockHttpServletRequest();
        MockHttpServletResponse responsed = new MockHttpServletResponse();

        service.doDelete(requestd,responsed);

        assert (service.getPersistency().getDB().isEmpty());

        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.setParameter("limit", "1");
        request2.setParameter("level", "DEBUG");
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        service.doGet(request2,response2);
        String s2 = response2.getContentAsString();
        String log2 = s2.substring(32);
        JSONArray jsonArray2 = new JSONArray(log2);
        assert (jsonArray2.isEmpty());
    }
}
