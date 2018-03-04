package cx.restclient.sast;

import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.sast.dto.*;
import cx.restclient.sast.exception.CxSASTException;

import java.io.IOException;

/**
 * Created by Galn on 05/02/2018.
 */
public interface ICxSASTClient {
    CreateScanResponse createSASTScan() throws CxSASTException, IOException, InterruptedException;
    SASTResults getSASTResults(CreateScanResponse createScanResponse);
    public void cancelSASTScan(String scanId) throws IOException, CxClientException;
}
