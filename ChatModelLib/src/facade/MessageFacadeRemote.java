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
import model.Message;

/**
 *
 * @author thomas
 */
@Remote
public interface MessageFacadeRemote {

    public void addMessage(String content, ChatUser user, Channel channel);

    public List<Message> getLatestMessagesOfChannel(Channel channel);

    public List<Message> getLatestMessagesOfUser(ChatUser user);
    
}
