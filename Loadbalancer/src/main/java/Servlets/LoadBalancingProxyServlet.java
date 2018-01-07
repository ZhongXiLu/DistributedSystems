/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.net.SocketTimeoutException;
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

/**
 *
 * @author thomas
 */
public class LoadBalancingProxyServlet extends ProxyServlet {

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
        //*/
    }

}
