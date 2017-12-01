
import ChatUser.ChatUser;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zhongxilu
 */
@Stateless
public class ChatUserFacade extends AbstractFacade<ChatUser> {

    @PersistenceContext(unitName = "ChatPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChatUserFacade() {
        super(ChatUser.class);
    }
    
    public boolean addUser(Integer id, String name, String password, Boolean isModerator) {
        if(!checkExists(id, name)) {
            try {
                // MD5 hashing
                String passwordMD5 = null;
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] tmp = password.getBytes();
                md5.update(tmp);
                passwordMD5 = byteArrToString(md5.digest());
                
                ChatUser u = new ChatUser(id, name, passwordMD5, true, isModerator);
                em.persist(u);
                return true;
            } catch(NoSuchAlgorithmException ex) {
                return false;
            }
        }
        return false;
    }
    
    private boolean checkExists(Integer id, String name) {
        Query q = em.createNamedQuery("ChatUser.findById");
        q.setParameter("id", id);
        Query q2 = em.createNamedQuery("ChatUser.findByName");
        q2.setParameter("name", name);
        return (!(q.getResultList().isEmpty()) || !(q2.getResultList().isEmpty()));
    }
    
    public boolean checkAccount(String username, String password) {
        ChatUser user = getChatUser(username);
        if(user != null) {
            // check password
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] tmp = password.getBytes();
                md5.update(tmp);
                String passwordMD5 = byteArrToString(md5.digest());
                if (user.getPassword().equals(passwordMD5)) {
                    return true;
                } else {
                    return false;
                }
            } catch(NoSuchAlgorithmException ex) {
                return false;
            }
            
        } else {
            // username doesnt exist
            return false;
        }
    }
    
    public ChatUser getChatUser(String username) {
        TypedQuery<ChatUser> q = em.createNamedQuery("ChatUser.findByName", ChatUser.class);
        q.setParameter("name", username);
        List<ChatUser> results = q.getResultList();
        if (results.isEmpty() || results.size() > 1) {
            // user doesnt exist (or for some reason there's more than one account linked to one name)
            return null;
        } else {
            return results.get(0);
        }
    }
    
    public List<ChatUser> getAllOnlineUsers() {
        TypedQuery<ChatUser> q = em.createNamedQuery("ChatUser.onlineUsers", ChatUser.class);
        return q.getResultList();
    }
    
    public void setIsOnline(String username, Boolean online) {
        ChatUser user = getChatUser(username);
        user.setIsOnline(online);
    }
        
    // Source: https://platform.netbeans.org/tutorials/60/nbm-login.html#md5
    private static String byteArrToString(byte[] b) {
        String res = null;
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int j = b[i] & 0xff;
            if (j < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(j));
        }
        res = sb.toString();
        return res.toUpperCase();
    }
    
}
