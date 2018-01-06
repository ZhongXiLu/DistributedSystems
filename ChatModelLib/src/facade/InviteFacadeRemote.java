/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import javax.ejb.Remote;
import model.ChatUser;
import model.Invite;

/**
 *
 * @author thomas
 */
@Remote
public interface InviteFacadeRemote {

    void addInvite(String channelName, ChatUser fromUser, String toUsername);

    void acceptInvite(Integer id);

    void declineInvite(Integer id);

    Invite getOpenInvite(ChatUser user);

}
