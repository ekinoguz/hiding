package translator.base;

import java.net.URI;

import javax.annotation.Nullable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author ekin
 *
 * Class to make a RESTful requests to translation API.
 * 
 * <p>Only Google is supported at the moment.
 */
public class Request {

  /**
   * Function to make a POST request to translation API.
   *
   * @param operation
   * @param query
   * @return output of the request in JSON format
   * @throws RuntimeException if there is a error with response
   */
  public static String makePostRequest(@Nullable String operation, String query) {
    try {
      Client client = Client.create();
      String path = "/language/translate/v2";
      if (operation != null) {
        path += "/" + operation;
      }
      WebResource webResource = client.resource(UriBuilder.fromUri("https://www.googleapis.com")
          .build());

      ClientResponse response = webResource
          .path(path)
          .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
          .header("X-HTTP-Method-Override", "GET")
//          .header("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7")
          .post(ClientResponse.class, query);

      if (response.getStatus() != 200) {
        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() +
            ", error:" + response.getEntity(String.class) + ", operation:" + operation +
            ", query:" + query);
      }
      return response.getEntity(String.class);//URLDecoder.decode(, "UTF-8");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null; // make the compiler happy
  }

  /**
   * Function to make a GET request to translation API.
   *
   * @param operation
   * @param query
   * @return output of the request in JSON format
   * @throws RuntimeException if there is a error with response
   */
  public static String makeGetRequest(@Nullable String operation, String query) {
    try {
      Client client = Client.create();
      
      String path = "/language/translate/v2";
      if (operation != null) {
        path += "/" + operation;
      }
      URI uri = new URI("https", "www.googleapis.com", path, query, null);
      String request = uri.toASCIIString();

      WebResource webResource = client.resource(request);
      ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

      if (response.getStatus() != 200) {
         throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() +
             ", error:" + response.getEntity(String.class) + ", operation:" + operation +
             ", query:" + query + ", request:" + request);
      }
      return response.getEntity(String.class);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null; // make the compiler happy
  }
}
