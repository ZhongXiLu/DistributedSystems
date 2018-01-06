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
public interface ChannelFacadeRemote {

    Channel getChannel(String name);

    Boolean switchChannel(ChatUser user, String channelName);

    Boolean removeChannel(String name);

    Boolean addPublicChannel(String name);

    Boolean addPrivateChannel(String name, ChatUser user1, ChatUser user2);

    List<Channel> getActivePublicChannels();

}
