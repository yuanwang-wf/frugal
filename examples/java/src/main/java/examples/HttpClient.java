package examples;

import com.workiva.frugal.FContext;
import com.workiva.frugal.protocol.FProtocolFactory;
import com.workiva.frugal.provider.FServiceProvider;
import com.workiva.frugal.transport.FHttpTransport;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import v1.music.Album;
import v1.music.FStore;

import java.io.IOException;

/**
 * Creates an HTTP client.
 */
public class HttpClient {

    public static void main(String[] args) throws TException, IOException {
        // Create an HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Create the HTTP transport using the client
        FHttpTransport transport = new FHttpTransport.Builder(httpClient, "http://localhost:9090/frugal").build();

        // Specify the protocol used for serializing requests.
        // Servers must use the same protocol stack
        FProtocolFactory protocolFactory = new FProtocolFactory(new TBinaryProtocol.Factory());

        // Create a new client for the music store
        FStore.Client storeClient = new FStore.Client(new FServiceProvider(transport, protocolFactory));

        // Request to buy an album
        Album album = storeClient.buyAlbum(new FContext("corr-id-1"), "ASIN-1290AIUBOA89", "ACCOUNT-12345");
        System.out.println("Bought the album: " + album);

        // Enter the contest
        storeClient.enterAlbumGiveaway(new FContext("corr-id-2"), "kevin@workiva.com", "Kevin");

        // Close the transport
        transport.close();

        // Close the http client
        httpClient.close();
    }
}
