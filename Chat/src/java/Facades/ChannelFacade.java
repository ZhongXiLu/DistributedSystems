package Facades;

import Facades.AbstractFacade;
import EntityClasses.Channel;
import EntityClasses.ChatUser;
import EntityClasses.Message;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    public Channel getChannel(String name) {
        TypedQuery<Channel> q = em.createNamedQuery("Channel.findByName", Channel.class);
        q.setParameter("name", name);
        List<Channel> results = q.getResultList();
        if (results.isEmpty() || results.size() > 1) {
            // channel doesnt exist
            return null;
        } else {
            return results.get(0);
        }
	}
	
	public Boolean switchChannel(ChatUser user, String channelName) {
		Channel oldChannel = user.getChannelId();
		if(!oldChannel.getIsPublic()) {
			// User was in private channel
			System.out.println("Test");
			// Make channel inactive
			oldChannel.setIsActive(false);
			// Move other User to default channel ("Welcome")
			Collection<ChatUser> users = oldChannel.getChatUserCollection();
			for (ChatUser userInOldChannel : users) {
				if(!userInOldChannel.equals(user)) {
					userInOldChannel.setChannelId(em.find(Channel.class, 0));	// first channel = default channel
				}
			}
		}
		user.setChannelId(getChannel(channelName));
		return true;
	}
	
	// Set channel to inactive (NOT deleting channel in db)
	public Boolean removeChannel(String name) {
		TypedQuery<Channel> q = em.createNamedQuery("Channel.findByName", Channel.class);
		q.setParameter("name", name);
		List<Channel> results = q.getResultList();
		if (results.isEmpty() || results.size() > 1) {
			// channel doesnt exist...
			return false;
		} else {
			results.get(0).setIsActive(false);
			return true;
		}
	}
	
	public Boolean addPublicChannel(String name) {
		if(!checkExists(name)) {
			Channel newChannel = new Channel(name, true, true);	// new public and active channel
			em.persist(newChannel);
			return true;
		}
		return false;
	}
	
	public Boolean addPrivateChannel(String name, ChatUser user1, String user2Name) {
		if(!checkExists(name)) {
			Channel newChannel = new Channel(name, false, true);	// new private and active channel
			
			user1.setChannelId(newChannel);			
			TypedQuery<ChatUser> q = em.createNamedQuery("ChatUser.findByName", ChatUser.class);
			q.setParameter("name", user2Name);
			ChatUser user2 = q.getResultList().get(0);
			user2.setChannelId(newChannel);
			em.persist(newChannel);
			return true;
		}
		return false;
	} 
	
	private boolean checkExists(String name) {
		Query q = em.createNamedQuery("Channel.findByName");
		q.setParameter("name", name);
		return !(q.getResultList().isEmpty());
	}
	
	public List<Channel> getActivePublicChannels() {
		TypedQuery<Channel> q = em.createNamedQuery("Channel.activePublic", Channel.class);
		return q.getResultList();
	}
	
	public List<Message> getLatestMessagesOfChannel(Channel channel) {
		TypedQuery<Message> q = em.createNamedQuery("Message.getLatestMessages", Message.class);
		q.setParameter("channel", channel);
		return q.setMaxResults(100).getResultList();
	}
	/*
	public void addMessage(ChatUser user, String message) {
                Channel channel = user.getChannelId();
		Message newMessage = new Message(message, channel, user);
		user.getMessageCollection().add(newMessage);
		channel.getMessageCollection().add(newMessage);
		em.persist(newMessage);
		//em.merge(channel);
                //System.out.println(newMessage);
	}
        */
}
