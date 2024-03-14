
package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 *
 * @author Andrew Nisbet andrew@dev-ils.com
 */
public class WebService 
{
    private final static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    public WebService() throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
//                .uri(URI.create("https://httpbin.org/get"))
                .uri(URI.create("https://dewey.polarislibrary.com/PAPIService/REST/public/v1/1033/100/1/api"))
                .setHeader("accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        HttpHeaders headers = response.headers();
        headers.map().forEach((k,v) -> System.out.println(k + ":" + v));

        // int value.
        System.out.println(response.statusCode());

        // Print the body
        System.out.println(response.body());
    }
}
