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
    
    private Boolean addChannel(String name, Boolean isPublic) {
        if (!checkExists(name)) {
            Channel newChannel = new Channel(name, isPublic, true);  // new public and active channel
            em.persist(newChannel);
            return true;
        }
        return false;
    }

    public Boolean addPublicChannel(String name) {
        return addChannel(name, true);
    }
    
    public Boolean addPrivateChannel(String name) {
        return addChannel(name, false);
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
        // TODO: does not work yet
        assert(user != null);
        assert(message != null);
        System.out.println(user.getId());
        System.out.println(user.getName());
        Channel channel = user.getChannelId();
        
        System.out.println(channel.getId());
        System.out.println(channel.getName());
                
        Collection<Message> messages = channel.getMessageCollection();           
        Message newMessage = new Message();
        newMessage.setContent(message);
        messages.add(newMessage);
        
        em.persist(newMessage);
        
        System.out.println(newMessage);
        System.out.println(messages);
        channel.setMessageCollection(messages);
    }
    */
}
