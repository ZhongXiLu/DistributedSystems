/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facades;

import EntityClasses.Channel;
import EntityClasses.ChatUser;
import EntityClasses.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author thomas
 */
@Stateless
public class MessageFacade extends AbstractFacade<Message> {

    @PersistenceContext(unitName = "ChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageFacade() {
        super(Message.class);
    }
    
    public void addMessage(String content, ChatUser user, Channel channel) {
        Message newMessage = new Message();
        newMessage.setContent(content);
        newMessage.setUserId(user);
        newMessage.setChannelId(channel);
        this.create(newMessage);
    }
	
	public List<Message> getLatestMessagesOfChannel(Channel channel) {
		TypedQuery<Message> q = em.createNamedQuery("Message.getLatestMessagesChannel", Message.class);
		q.setParameter("channel", channel);
		return q.setMaxResults(100).getResultList();
	}
	
	public List<Message> getLatestMessagesOfUser(ChatUser user) {
		TypedQuery<Message> q = em.createNamedQuery("Message.getLatestMessagesUser", Message.class);
		q.setParameter("user", user);
		return q.setMaxResults(100).getResultList();
	}
    
}
