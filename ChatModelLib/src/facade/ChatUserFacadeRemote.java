/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import java.util.List;
import javax.ejb.Remote;
import model.Channel;
import model.ChatUser;

/**
 *
 * @author thomas
 */
@Remote
public interface ChatUserFacadeRemote {

    boolean addUser(String name, String password, Boolean isModerator);

    Channel getChannelOfUser(String username);

    boolean checkAccount(String username, String password);

    ChatUser getChatUser(String username);

    List<ChatUser> getAllOnlineUsers();

    void setIsOnline(String username, Boolean online);

    void refreshUser(String username);

}
