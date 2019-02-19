package izzzyx.common.tools.handler;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;


public class DefaultResponseHandler implements ResponseHandler<HttpResponse> {


    @Override
    public HttpResponse handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        return httpResponse;
    }

}
