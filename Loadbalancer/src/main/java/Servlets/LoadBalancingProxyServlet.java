/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;

import org.mitre.dsmiley.httpproxy.ProxyServlet;

/**
 *
 * @author thomas
 */
public class LoadBalancingProxyServlet extends ProxyServlet {

    /*class ListedServer {

        private String url;
        private int coolDown;

        ListedServer(String serverUrl) {
            url = serverUrl;
            coolDown = 0;
        }

        public String getUrl() {
            
            return url;
        }
        public boolean isAvailable() {
            return coolDown == 0;
        }
        public void setOffline() {
            
        }
    }*/
    private List<String> urlList = Arrays.asList(
            "143.129.78.107"
            ,"143.129.78.108"
    );

    private final AtomicInteger nextServerId = new AtomicInteger();

    private final Semaphore semaphore = new Semaphore(1, true);
    
    private boolean successfullProxyRequest;
    
    private String getNextHost() {
        String result = urlList.get(nextServerId.getAndIncrement() % urlList.size());
        return result;
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        //initialize request attributes from caches if unset by a subclass by this point
        try {
            semaphore.acquire(1);
            successfullProxyRequest = false;
            while (!successfullProxyRequest) {
                String uri = getNextHost();
                servletRequest.setAttribute(ATTR_TARGET_URI, "/Chat");
                HttpHost host = new HttpHost(uri, 8080);
                servletRequest.setAttribute(ATTR_TARGET_HOST, host);
                successfullProxyRequest = true;
                super.service(servletRequest, servletResponse);
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
