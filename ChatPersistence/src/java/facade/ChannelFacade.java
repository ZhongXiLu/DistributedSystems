package facade;

import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.Channel;
import model.ChatUser;

/**
 *
 * @author thomas
 */
@Stateless
//@Remote(ChannelFacadeRemote.class)
public class ChannelFacade extends AbstractFacade<Channel> implements facade.ChannelFacadeRemote {

    @PersistenceContext(unitName = "ChatPersistencePU")
    private EntityManager em;

    @EJB
    private ChatUserFacade chatUserFacade;
    
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
        Channel oldChannel = chatUserFacade.getChannelOfUser(user.getName());
        if (!oldChannel.getIsPublic()) {
            // User was in private channel
            em.refresh(oldChannel);
            // Make channel inactive
            oldChannel.setIsActive(false);
            this.edit(oldChannel);
            // Move other User to default channel ("Welcome")
            Collection<ChatUser> users = oldChannel.getChatUserCollection();
            for (ChatUser userInOldChannel : users) {
                if (!userInOldChannel.equals(user)) {
                    userInOldChannel.setChannelId(this.find(1));	// first channel = default channel
                    chatUserFacade.edit(userInOldChannel);
                }
            }
        }
        user.setChannelId(getChannel(channelName));
        chatUserFacade.edit(user);
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
        if (!checkExists(name)) {
            Channel newChannel = new Channel(name, true, true);	// new public and active channel
            this.create(newChannel);
            return true;
        }
        return false;
    }

    public Boolean addPrivateChannel(String name, ChatUser user1, ChatUser user2) {
        if (!checkExists(name)) {
            Channel newChannel = new Channel(name, false, true);	// new private and active channel

            user1.setChannelId(newChannel);
            user2.setChannelId(newChannel);
            this.create(newChannel);
            chatUserFacade.edit(user1);
            chatUserFacade.edit(user2);
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
}
