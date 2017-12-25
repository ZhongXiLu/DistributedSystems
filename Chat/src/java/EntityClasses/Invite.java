/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityClasses;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zhongxilu
 */
@Entity
@Table(name = "INVITE")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Invite.findAll", query = "SELECT i FROM Invite i")
	, @NamedQuery(name = "Invite.getOpenInvites", query = "SELECT i FROM Invite i WHERE i.toUser = :user AND i.isActive = TRUE AND i.isRead = FALSE")
	, @NamedQuery(name = "Invite.findById", query = "SELECT i FROM Invite i WHERE i.id = :id")
	, @NamedQuery(name = "Invite.findByName", query = "SELECT i FROM Invite i WHERE i.name = :name")
	, @NamedQuery(name = "Invite.findByIsActive", query = "SELECT i FROM Invite i WHERE i.isActive = :isActive")
	, @NamedQuery(name = "Invite.findByIsRead", query = "SELECT i FROM Invite i WHERE i.isRead = :isRead")})
public class Invite implements Serializable {

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
    @Column(name = "IS_ACTIVE")
	private Boolean isActive;
	@Basic(optional = false)
    @NotNull
    @Column(name = "IS_READ")
	private Boolean isRead;
	@JoinColumn(name = "TO_USER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
	private ChatUser toUser;
	@JoinColumn(name = "FROM_USER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
	private ChatUser fromUser;

	public Invite() {
	}

	public Invite(Integer id) {
		this.id = id;
	}
	
	public Invite(String name, ChatUser from, ChatUser to, Boolean isActive, Boolean isRead) {
		this.name = name;
		this.fromUser = from;
		this.toUser = to;
		this.isActive = isActive;
		this.isRead = isRead;
	}
	
	public Invite(Integer id, String name, Boolean isActive, Boolean isRead) {
		this.id = id;
		this.name = name;
		this.isActive = isActive;
		this.isRead = isRead;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public ChatUser getToUser() {
		return toUser;
	}

	public void setToUser(ChatUser toUser) {
		this.toUser = toUser;
	}

	public ChatUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(ChatUser fromUser) {
		this.fromUser = fromUser;
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
		if (!(object instanceof Invite)) {
			return false;
		}
		Invite other = (Invite) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EntityClasses.Invite[ id=" + id + " ]";
	}
	
}
