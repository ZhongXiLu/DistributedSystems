/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatUser;

import Channel.Channel;
import Message.Message;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author zhongxilu
 */
@Entity
@Table(name = "CHAT_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatUser.findAll", query = "SELECT c FROM ChatUser c")
    , @NamedQuery(name = "ChatUser.onlineUsers", query = "SELECT c FROM ChatUser c WHERE c.isOnline = 1")
    , @NamedQuery(name = "ChatUser.findById", query = "SELECT c FROM ChatUser c WHERE c.id = :id")
    , @NamedQuery(name = "ChatUser.findByName", query = "SELECT c FROM ChatUser c WHERE c.name = :name")
    , @NamedQuery(name = "ChatUser.findByPassword", query = "SELECT c FROM ChatUser c WHERE c.password = :password")
    , @NamedQuery(name = "ChatUser.findByIsOnline", query = "SELECT c FROM ChatUser c WHERE c.isOnline = :isOnline")
    , @NamedQuery(name = "ChatUser.findByIsModerator", query = "SELECT c FROM ChatUser c WHERE c.isModerator = :isModerator")})
public class ChatUser implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Message> messageCollection;

    @JoinTable(name = "CHANNEL_USER", joinColumns = {
        @JoinColumn(name = "USER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CHANNEL_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Collection<Channel> channelCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_ONLINE")
    private Boolean isOnline;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_MODERATOR")
    private Boolean isModerator;

    public ChatUser() {
    }

    public ChatUser(Integer id) {
        this.id = id;
    }

    public ChatUser(Integer id, String name, String password, Boolean isOnline, Boolean isModerator) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isOnline = isOnline;
        this.isModerator = isModerator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Boolean getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(Boolean isModerator) {
        this.isModerator = isModerator;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChatUser)) {
            return false;
        }
        ChatUser other = (ChatUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChatUser.ChatUser[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Channel> getChannelCollection() {
        return channelCollection;
    }

    public void setChannelCollection(Collection<Channel> channelCollection) {
        this.channelCollection = channelCollection;
    }

    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }
    
}
