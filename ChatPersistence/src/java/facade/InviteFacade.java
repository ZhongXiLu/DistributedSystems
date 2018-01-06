/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import model.ChatUser;
import model.Invite;

/**
 *
 * @author thomas
 */
@Stateless
public class InviteFacade extends AbstractFacade<Invite> implements InviteFacadeRemote {

    @PersistenceContext(unitName = "ChatPersistencePU")
    private EntityManager em;

    @EJB
    private ChannelFacade channelFacade;

    @EJB
    private ChatUserFacade chatUserFacade;

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
        if (openInvites.size() > 0) {
            return openInvites.get(0);
        }
        return null;	// no open invites
    }
}
