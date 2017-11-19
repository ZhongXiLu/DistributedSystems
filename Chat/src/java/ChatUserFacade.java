
import chat_user.ChatUser;
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
        if(!checkExists(id)) {
            ChatUser u = new ChatUser(id, name, password, isModerator);
            em.persist(u);
            return true;
        }
        return false;
    }
    
    private boolean checkExists(Integer id) {
        Query q = em.createNamedQuery("ChatUser.findById");
        q.setParameter("id", id);
        return !(q.getResultList().isEmpty());
    }
    
    public boolean checkAccount(String username, String password) {
        TypedQuery<ChatUser> q = em.createNamedQuery("ChatUser.findByName", ChatUser.class);
        q.setParameter("name", username);
        List<ChatUser> results = q.getResultList();
        if(results.isEmpty() || results.size() > 1) {
            // user doesnt exist (or for some reason there's more than one account linked to one name)
            return false;
        } else {
            // check password
            if(results.get(0).getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
            
        }
    }
}
