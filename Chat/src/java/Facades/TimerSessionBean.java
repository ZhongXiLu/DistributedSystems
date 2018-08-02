/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import EntityClasses.ChatUser;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author zhongxilu
 */
@Singleton
@Startup
public class TimerSessionBean {

	@EJB
	private ChatUserFacade chatUserFacade;
        
        @PostConstruct
        public void init() {
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    checkOnlineUsers();
                }
            }, 0, 10, TimeUnit.SECONDS);    // schedule every 30 seconds
        }

        public void checkOnlineUsers() {
            List<ChatUser> onlineUsers = chatUserFacade.getAllOnlineUsers();
            Date currentTime = new Date();
            for (ChatUser user : onlineUsers) {
                long difference = Math.abs(currentTime.getTime() - chatUserFacade.getLastOnline(user).getTime()) / 1000;		// in seconds
                //System.out.println("last online: " + user.getName() + " : " + chatUserFacade.getLastOnline(user).getTime());
                if (difference > 30) {
                    //System.out.println("user is offline: " + user.getName());
                    // no request from this user in the past 30 seconds -> force log out
                    chatUserFacade.setIsOnline(user.getName(), false);
                }
            }
        }
        
	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")
}
