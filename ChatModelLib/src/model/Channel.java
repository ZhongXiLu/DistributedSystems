/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thomas
 */
@Entity
@Table(name = "CHANNEL")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Channel.findAll", query = "SELECT c FROM Channel c")
	, @NamedQuery(name = "Channel.activePublic", query = "SELECT c FROM Channel c WHERE c.isPublic = TRUE AND c.isActive = TRUE")
	, @NamedQuery(name = "Channel.findById", query = "SELECT c FROM Channel c WHERE c.id = :id")
	, @NamedQuery(name = "Channel.findByName", query = "SELECT c FROM Channel c WHERE c.name = :name")
	, @NamedQuery(name = "Channel.findByIsPublic", query = "SELECT c FROM Channel c WHERE c.isPublic = :isPublic")
	, @NamedQuery(name = "Channel.findByIsActive", query = "SELECT c FROM Channel c WHERE c.isActive = :isActive")})
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "IS_PUBLIC")
    private Boolean isPublic;
    @Basic(optional = false)
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channelId")
    private Collection<Message> messageCollection;
    @OneToMany(mappedBy = "channelId")
    private Collection<ChatUser> chatUserCollection;

    public Channel() {
    }

    public Channel(Integer id) {
        this.id = id;
    }

    public Channel(String name, Boolean isPublic, Boolean isActive) {
        this.name = name;
        this.isPublic = isPublic;
        this.isActive = isActive;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    @XmlTransient
    public Collection<ChatUser> getChatUserCollection() {
        return chatUserCollection;
    }

    public void setChatUserCollection(Collection<ChatUser> chatUserCollection) {
        this.chatUserCollection = chatUserCollection;
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
        if (!(object instanceof Channel)) {
            return false;
        }
        Channel other = (Channel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Channel[ id=" + id + " ]";
    }
    
}
