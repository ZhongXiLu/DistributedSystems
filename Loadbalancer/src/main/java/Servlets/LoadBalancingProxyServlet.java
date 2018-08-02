/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;

import org.mitre.dsmiley.httpproxy.ProxyServlet;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.EntityUtils;
import org.apache.http.conn.ConnectTimeoutException;

/**
 *
 * @author thomas
 */
public class LoadBalancingProxyServlet extends ProxyServlet {

    private final List<String> uriList = Arrays.asList(
            "http://www.google.com/",
            "http://192.168.1.200/"
    );
    private final AtomicInteger nextServerId = new AtomicInteger();

    private String getNextUri() {
        String result = uriList.get(nextServerId.getAndIncrement() % uriList.size());
        return result;
    }

    /*@Override
    protected RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setRedirectsEnabled(doHandleRedirects)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES) // we handle them in the servlet instead
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(readTimeout)
                .setRelativeRedirectsAllowed(true)
                .build();
    }*/
    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        targetUri = getNextUri();
        
        service(servletRequest, servletResponse, targetUri);
    }
    
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String targetUri)
            throws ServletException, IOException {      
        
        if (targetUri == null) {
            throw new ServletException(P_TARGET_URI + " is required.");
        }
        //test it's valid
        try {
            targetUriObj = new URI(targetUri);
        } catch (Exception e) {
            throw new ServletException("Trying to process targetUri init parameter: " + e, e);
        }
        HttpHost targetHost = URIUtils.extractHost(targetUriObj);
        
        //initialize request attributes from caches if unset by a subclass by this point
        if (servletRequest.getAttribute(ATTR_TARGET_URI) == null) {
            servletRequest.setAttribute(ATTR_TARGET_URI, targetUri);
            System.out.println("-- SET TARGET URI --");
        }
        else {
            System.out.println("-- USED URI CACHE --");
        }
        if (servletRequest.getAttribute(ATTR_TARGET_HOST) == null) {
            servletRequest.setAttribute(ATTR_TARGET_HOST, targetHost);
            System.out.println("-- SET TARGET URL --");
        }
        else {
            System.out.println("-- USED URL CACHE --");
        }
        
        servletRequest.setAttribute(ATTR_TARGET_URI, targetUri);
        servletRequest.setAttribute(ATTR_TARGET_HOST, targetHost);
        
        System.out.println("TARGET_URI: " + servletRequest.getAttribute(ATTR_TARGET_URI));
        System.out.println("TARGET_HOST: " + servletRequest.getAttribute(ATTR_TARGET_HOST));
        
        // Make the Request
        //note: we won't transfer the protocol version because I'm not sure it would truly be compatible
        String method = servletRequest.getMethod();
        String proxyRequestUri = rewriteUrlFromRequest(servletRequest);
        HttpRequest proxyRequest;
        //spec: RFC 2616, sec 4.3: either of these two headers signal that there is a message body.
        if (servletRequest.getHeader(HttpHeaders.CONTENT_LENGTH) != null
                || servletRequest.getHeader(HttpHeaders.TRANSFER_ENCODING) != null) {
            proxyRequest = newProxyRequestWithEntity(method, proxyRequestUri, servletRequest);
        } else {
            proxyRequest = new BasicHttpRequest(method, proxyRequestUri);
        }

        copyRequestHeaders(servletRequest, proxyRequest);

        setXForwardedForHeader(servletRequest, proxyRequest);

        HttpResponse proxyResponse = null;
        try {
            System.out.println("doExecuteTargetHost: " + getTargetHost(servletRequest));

            // Execute the request
            proxyResponse = doExecute(servletRequest, servletResponse, proxyRequest);

            // Process the response:
            // Pass the response code. This method with the "reason phrase" is deprecated but it's the
            //   only way to pass the reason along too.
            int statusCode = proxyResponse.getStatusLine().getStatusCode();
            //noinspection deprecation
            servletResponse.setStatus(statusCode, proxyResponse.getStatusLine().getReasonPhrase());

            // Copying response headers to make sure SESSIONID or other Cookie which comes from the remote
            // server will be saved in client when the proxied url was redirected to another one.
            // See issue [#51](https://github.com/mitre/HTTP-Proxy-Servlet/issues/51)
            copyResponseHeaders(proxyResponse, servletRequest, servletResponse);

            //for (String name : servletResponse.getHeaderNames())
            //    System.out.println("" + name);
            System.out.println("StatusCode: " + statusCode);

            if (statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
                // 304 needs special handling.  See:
                // http://www.ics.uci.edu/pub/ietf/http/rfc1945.html#Code304
                // Don't send body entity/content!
                servletResponse.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
            } else {
                // Send the content to the client
                copyResponseEntity(proxyResponse, servletResponse, proxyRequest, servletRequest);
            }

        } catch (Exception e) {
            if (e instanceof ConnectTimeoutException) {
                System.out.println("** Exception happened: ConnectionTimeout **");
                service(servletRequest, servletResponse);
            }
            else if (proxyResponse == null) {
                System.out.println("** Exception happened: ProxyResponse == null **");
                service(servletRequest, servletResponse);
            }
            else {
                int statusCode = proxyResponse.getStatusLine().getStatusCode();
                System.out.println("** Exception happened: code " + statusCode + " **");
                if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
                    service(servletRequest, servletResponse);
                }
            }
            if (proxyRequest instanceof AbortableHttpRequest) {
                AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest) proxyRequest;
                abortableHttpRequest.abort();
            }
            //handleRequestException(proxyRequest, e);
        } finally {
            // make sure the entire entity was consumed, so the connection is released
            if (proxyResponse != null) {
                EntityUtils.consumeQuietly(proxyResponse.getEntity());
            }
            //Note: Don't need to close servlet outputStream:
            // http://stackoverflow.com/questions/1159168/should-one-call-close-on-httpservletresponse-getoutputstream-getwriter
        }
    }
    
    protected void handleRequestException(HttpRequest proxyRequest, Exception e) throws ServletException, IOException {
        //abort request, according to best practice with HttpClient
        if (proxyRequest instanceof AbortableHttpRequest) {
          AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest) proxyRequest;
          abortableHttpRequest.abort();
        }
        if (e instanceof RuntimeException)
          throw (RuntimeException)e;
        if (e instanceof ServletException)
          throw (ServletException)e;
        //noinspection ConstantConditions
        if (e instanceof IOException)
          throw (IOException) e;
        throw new RuntimeException(e);
    }

    private void setXForwardedForHeader(HttpServletRequest servletRequest,
            HttpRequest proxyRequest) {
        if (doForwardIP) {
            String forHeaderName = "X-Forwarded-For";
            String forHeader = servletRequest.getRemoteAddr();
            String existingForHeader = servletRequest.getHeader(forHeaderName);
            if (existingForHeader != null) {
                forHeader = existingForHeader + ", " + forHeader;
            }
            proxyRequest.setHeader(forHeaderName, forHeader);

            String protoHeaderName = "X-Forwarded-Proto";
            String protoHeader = servletRequest.getScheme();
            proxyRequest.setHeader(protoHeaderName, protoHeader);
        }
    }

    /*
    private final List<String> urlList = Arrays.asList(
            "192.168.1.54",
             "192.168.1.68"
    );

    private final AtomicInteger nextServerId = new AtomicInteger();

    private final Semaphore semaphore = new Semaphore(1, true);

    private boolean successfullProxyRequest;

    private String getNextHost() {
        String result = urlList.get(nextServerId.getAndIncrement() % urlList.size());
        return result;
    }

    @Override
    protected RequestConfig buildRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom()
                .setRedirectsEnabled(false)//doHandleRedirects)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES) // we handle them in the servlet instead
                .setConnectTimeout(500)
                .setSocketTimeout(500);
        return builder.build();
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        //initialize request attributes from caches if unset by a subclass by this point
        try {
            semaphore.acquire(1);
            successfullProxyRequest = false;
            int nrRequestsMade = 0;
            while (!successfullProxyRequest && nrRequestsMade < urlList.size() + 1) {
                String ip = getNextHost();
                servletRequest.setAttribute(ATTR_TARGET_URI, "/Chat");
                HttpHost host = new HttpHost(ip, 8080);
                servletRequest.setAttribute(ATTR_TARGET_HOST, host);
                successfullProxyRequest = true;
                //HttpServletRequest newServletRequest = new HttpServletRequestWrapper(servletRequest);
                try {
                    super.service(servletRequest, servletResponse);
                } catch (SocketTimeoutException e) {
                    successfullProxyRequest = false;
                }
                nrRequestsMade += 1;
                if (servletResponse.getStatus() >= 400) {
                    successfullProxyRequest = false;
                }
                
            }

            System.out.println("ATTR_TARGET_URI: " + servletRequest.getAttribute(ATTR_TARGET_URI));
            System.out.println("ATTR_TARGET_HOST: " + servletRequest.getAttribute(ATTR_TARGET_HOST));
        } catch (InterruptedException e) {
            System.err.print(e);
        } finally {
            semaphore.release(1);
        }
    }

    @Override
    protected void handleRequestException(HttpRequest proxyRequest, Exception e) throws ServletException, IOException {
        //abort request, according to best practice with HttpClient

        super.handleRequestException(proxyRequest, e);
        successfullProxyRequest = false;
        /*
        if (proxyRequest instanceof AbortableHttpRequest) {
            AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest) proxyRequest;
            abortableHttpRequest.abort();
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        if (e instanceof ServletException) {
            throw (ServletException) e;
        }
        //noinspection ConstantConditions
        if (e instanceof IOException) {
            throw (IOException) e;
        }
        throw new RuntimeException(e);
        //*
    }*/
}
