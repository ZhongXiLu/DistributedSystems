/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import EntityClasses.ChatUser;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author zhongxilu
 */
@Stateless
public class TimerSessionBean extends AbstractFacade<ChatUser> {

	@EJB
	private ChatUserFacade chatUserFacade;
	
	@PersistenceContext(unitName = "ChatPU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public TimerSessionBean() {
		super(ChatUser.class);
	}
	
	@Schedule(dayOfWeek = "*", month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "*", second = "*/30", persistent = false)
	public void myTimer() {
		TypedQuery<ChatUser> q = em.createNamedQuery("ChatUser.onlineUsers", ChatUser.class);
		List<ChatUser> onlineUsers = q.getResultList();
		Date currentTime = new Date();
		for(ChatUser user: onlineUsers) {
			long difference = Math.abs(currentTime.getTime() - user.getLastOnline().getTime())/1000;		// in seconds
			if(difference > 30) {
				// no request from this user in the past 30 seconds -> force log out
				chatUserFacade.setIsOnline(user.getName(), false);
			}
			
		}
	}

	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")
}
