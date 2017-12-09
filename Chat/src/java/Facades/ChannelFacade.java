package Facades;


import Facades.AbstractFacade;
import EntityClasses.Channel;
import EntityClasses.ChatUser;
import EntityClasses.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zhongxilu
 */
@Stateless
public class ChannelFacade extends AbstractFacade<Channel> {

	@PersistenceContext(unitName = "ChatPU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public ChannelFacade() {
		super(Channel.class);
	}
	
	public List<Channel> getPublicChannels() {
		TypedQuery<Channel> q = em.createNamedQuery("Channel.findByIsPublic", Channel.class);
		q.setParameter("isPublic", true);
		return q.getResultList();
	}
	
	public Channel getChannelOfUser(ChatUser user) {
		return user.getChannelId();
	}
	
	public List<Message> getLatestMessagesOfChannel(int channelId) {
		TypedQuery<Message> q = em.createNamedQuery("Channel.findByIsPublic", Message.class);
		q.setParameter("channelId", channelId);
		return q.setMaxResults(100).getResultList();
	}
}
