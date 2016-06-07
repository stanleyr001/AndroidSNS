package main.java.Users;

import java.io.IOException;

/*
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
*/
public class RESTLogin {

    private String ENDPOINT;

    public RESTLogin( String end )
    {
        ENDPOINT = end;
    }

    public String login(String u, String p)
    {
        // Original method
        String authCookie = null;
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(ENDPOINT + "sessions");

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        NameValuePair[] data =
                {
                        new NameValuePair("username", u),
                        new NameValuePair("password", p),
                        new NameValuePair("on_success", ENDPOINT.replaceAll("api/2/",
                                "") + "util-iframes/blank-page.html?response=success"),
                        new NameValuePair("on_fail", ENDPOINT.replaceAll("api/2/", "")
                                + "util-iframes/blank-page.html?response=fail")
                };

        method.setRequestBody( data );

        try
        {

            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_SEE_OTHER)
            {
                //System.err.println("Method failed: " + method.getStatusLine());
            }
            // Read the response header location to determine success or
            // failure.
            String location = method.getResponseHeader("Location").getValue();

            if (location.contains("response=success"))
            {
                authCookie = method.getResponseHeader("Set-Cookie").getValue();
            }
            else
            {
                //System.err.println("Login failed");
            }

        } catch (HttpException e) {
            //System.err.println("Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            //System.err.println("Fatal transport error: " + e.getMessage());
            //e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

        return authCookie;
    }

}