/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityClasses;

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
import javax.persistence.ManyToOne;
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "toUser")
	private Collection<Invite> inviteCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fromUser")
	private Collection<Invite> inviteCollection1;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
	private Collection<Message> messageCollection;

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
	@JoinColumn(name = "CHANNEL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
	private Channel channelId;

	public ChatUser() {
	}

	public ChatUser(Integer id) {
		this.id = id;
	}

	public ChatUser(String name, String password, Boolean isOnline, Boolean isModerator) {
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

	public Channel getChannelId() {
		return channelId;
	}

	public void setChannelId(Channel channelId) {
                Channel oldChannelId = this.channelId;
                //assert(this.channelId != channelId);
		this.channelId = channelId;
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
		return "EntityClasses.ChatUser[ id=" + id + " ]";
	}

	@XmlTransient
	public Collection<Message> getMessageCollection() {
		return messageCollection;
	}

	public void setMessageCollection(Collection<Message> messageCollection) {
		this.messageCollection = messageCollection;
	}

	@XmlTransient
	public Collection<Invite> getInviteCollection() {
		return inviteCollection;
	}

	public void setInviteCollection(Collection<Invite> inviteCollection) {
		this.inviteCollection = inviteCollection;
	}

	@XmlTransient
	public Collection<Invite> getInviteCollection1() {
		return inviteCollection1;
	}

	public void setInviteCollection1(Collection<Invite> inviteCollection1) {
		this.inviteCollection1 = inviteCollection1;
	}

}
