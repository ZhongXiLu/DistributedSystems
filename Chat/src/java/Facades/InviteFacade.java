/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import EntityClasses.ChatUser;
import EntityClasses.Invite;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author zhongxilu
 */
@Stateless
public class InviteFacade extends AbstractFacade<Invite> {

	@EJB
	private ChannelFacade channelFacade;

	@EJB
	private ChatUserFacade chatUserFacade;

	@PersistenceContext(unitName = "ChatPU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public InviteFacade() {
		super(Invite.class);
	}
	
	public void addInvite(String channelName, ChatUser fromUser, String toUsername) {
		ChatUser toUser = chatUserFacade.getChatUser(toUsername);
		Invite invite = new Invite(channelName, fromUser, toUser, true, false);
		this.create(invite);
	}

	public void acceptInvite(Integer id) {
		Invite invite = this.find(id);
		invite.setIsActive(false);
		invite.setIsRead(true);
		channelFacade.addPrivateChannel(invite.getName(), invite.getFromUser(), invite.getToUser());
		this.edit(invite);
	}

	public void declineInvite(Integer id) {
		Invite invite = this.find(id);
		invite.setIsActive(false);
		invite.setIsRead(true);
		this.edit(invite);
	}
	
	// Get the oldest open (active and not read) invite of an ChatUser
	public Invite getOpenInvite(ChatUser user) {
		TypedQuery<Invite> q = em.createNamedQuery("Invite.getOpenInvites", Invite.class);
		q.setParameter("user", user);
		List<Invite> openInvites = q.getResultList();
		if(openInvites.size() > 0) {
			return openInvites.get(0);
		}
		return null;	// no open invites
	}
}
