/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zhongxilu
 */
@Entity
@Table(name = "MESSAGE")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m")
	, @NamedQuery(name = "Message.getLatestMessages", query = "SELECT m FROM Message m WHERE m.channelId = :channel ORDER BY m.timestamp ASC")
	, @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id")
	, @NamedQuery(name = "Message.findByContent", query = "SELECT m FROM Message m WHERE m.content = :content")
	, @NamedQuery(name = "Message.findByTimestamp", query = "SELECT m FROM Message m WHERE m.timestamp = :timestamp")})
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
	private Integer id;
	@Size(max = 2000)
    @Column(name = "CONTENT")
	private String content;
	@Basic(optional = false)
    @NotNull
    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	@JoinColumn(name = "CHANNEL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
	private Channel channelId;
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
	private ChatUser userId;

	public Message() {
	}

	public Message(Integer id) {
		this.id = id;
	}

	public Message(Integer id, Date timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}
	
	public Message(String content, Channel channel, ChatUser user) {
		this.content = content;
		this.channelId = channel;
		this.userId = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Channel getChannelId() {
		return channelId;
	}

	public void setChannelId(Channel channelId) {
		this.channelId = channelId;
	}

	public ChatUser getUserId() {
		return userId;
	}

	public void setUserId(ChatUser userId) {
		this.userId = userId;
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
		if (!(object instanceof Message)) {
			return false;
		}
		Message other = (Message) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EntityClasses.Message[ id=" + id + " ]";
	}
	
}
