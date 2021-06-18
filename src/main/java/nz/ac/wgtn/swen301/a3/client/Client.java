package nz.ac.wgtn.swen301.a3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.methods.HttpGet;

import java.net.ConnectException;
import java.net.URI;

/**
 * The type Client.
 */
public class Client {


    /**
     * The entry point of application.
     *
     * @param arg the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] arg) throws Exception {
        String servlet = null;
        String type = arg[0];
        String filename = arg[1];
//        String type = "excel";
//        String filename = "logs321.xls";
        String Path="";
        if (type.equals("excel")) {
            servlet = "statsxls";
            Path ="/resthome4logs/statsxls";

        } else if (type.equals("csv")) {
            servlet = "statscsv";
            Path ="/resthome4logs/statscsv";
        }

        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http")
                    .setHost("localhost")
                    .setPath(Path)
                    .setPort(8080)
                    .setParameter("filename", filename);
            URI uri = builder.build();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(uri);
            StringEntity entity = new StringEntity("");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, servlet));
            HttpResponse response = httpClient.execute(request);

            String content = EntityUtils.toString(response.getEntity());
        }
        catch (ConnectException e){
            System.out.println("connection exception");
            e.printStackTrace();
        }
    }
}
