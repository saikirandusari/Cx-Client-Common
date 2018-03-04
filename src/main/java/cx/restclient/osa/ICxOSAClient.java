package cx.restclient.osa;

import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.osa.dto.CreateOSAScanResponse;
import cx.restclient.osa.dto.OSAResults;
import cx.restclient.osa.exception.CxOSAException;

import java.io.IOException;

/**
 * Created by Galn on 07/02/2018.
 */
public interface ICxOSAClient {

    public CreateOSAScanResponse createOSAScan() throws IOException, InterruptedException, CxClientException ;

    public OSAResults getOSAResults(String scanId) throws CxClientException, IOException, CxOSAException, InterruptedException;
}
