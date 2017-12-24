/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Registration;

import javax.ejb.Stateless;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

/**
 *
 * @author thomas
 */
@Stateless
@ServiceMode(value = javax.xml.ws.Service.Mode.PAYLOAD)
@WebServiceProvider(serviceName = "ServerPollingService", portName = "ServerDiscoverServicePort", targetNamespace = "http://LoadBalancer/", wsdlLocation = "WEB-INF/wsdl/LBRegistrationWebService/wsit-LoadBalancer.ServerDiscoverService.xml.wsdl")
public class LBRegistrationWebService implements javax.xml.ws.Provider<javax.xml.transform.Source> {

    public javax.xml.transform.Source invoke(javax.xml.transform.Source source) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
