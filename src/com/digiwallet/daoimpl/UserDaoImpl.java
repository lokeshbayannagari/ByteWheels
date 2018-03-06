package com.digiwallet.daoimpl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.digiwallet.dao.UserDaoActions;
import com.digiwallet.entity.Card;
import com.digiwallet.entity.Role;
import com.digiwallet.entity.User;
import com.digiwallet.exception.WalletException;
import com.digiwallet.utils.RandomStringGenerator;
import com.digiwallet.utils.WalletEncryptor;
import com.digiwallet.utils.WalletLogger;

@Stateless
public class UserDaoImpl implements UserDaoActions{
	WalletLogger logger = new WalletLogger(WalletEncryptor.class);
	
    @PersistenceContext
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void save(Object obj)
    {
        this.entityManager.persist(obj);
        this.entityManager.flush();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void delete(Object obj)
    {
        this.entityManager.remove(obj);
        this.entityManager.flush();
    }
    
    @Override 
    public String getUserCardDetails() throws WalletException {
    	try {
    		JSONArray users = new JSONArray();
			TypedQuery<User> q = this.entityManager.createNamedQuery("User.findAll", User.class);
			List<User> userList = q.getResultList();
			for(User user : userList){
				JSONObject userJSON = new JSONObject();
				userJSON.put("userid", user.getUserid());
				userJSON.put("username", user.getUsername());
				userJSON.put("emailid", user.getEmailid());
				userJSON.put("roleid", user.getRole().getRoleid());
				
				JSONArray cards = new JSONArray();
				for(Card card : user.getCards()){
					JSONObject cardJSON = new JSONObject();
					cardJSON.put("cardid", card.getCardid());
					cardJSON.put("userid", card.getUser().getUserid());
					cardJSON.put("cardtypeid", card.getCardtype().getCardtypeid());
					cardJSON.put("expirydate", card.getExpirydate());
					cardJSON.put("cardnumber", card.getCardnumber());
					cards.add(cardJSON);
				}
				userJSON.put("cards", cards);
				users.add(userJSON);
			}
			return users.toString();
		} catch (JSONException e) {
			logger.error("exception " + e.getErrorcode());
			throw new WalletException(e.getErrorcode());
		} catch (Exception e) {
			logger.error("exception " + e.getMessage());
			throw new WalletException(e.getMessage());
		}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String addUserDetails(String userData) throws WalletException {
    	JSONObject userJSON = new JSONObject(userData);
    	User user = new User();
    	user.setUsername(userJSON.getString("username"));
    	user.setEmailid(userJSON.getString("emailid"));
    	user.setRole(new Role(2));
    	RandomStringGenerator stringGenerator = new RandomStringGenerator(WalletConstants.DEFAULT_PASSWORD_LENGTH);
    	String password = userJSON.has("password") ? userJSON.getString("password") : stringGenerator.generateString();
    	user.setPassword(password);
    	this.save(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String updateUserDetails(String userData) throws WalletException {
    	JSONObject userJSON = new JSONObject(userData);
    	User user = this.entityManager.find(User.class, userJSON.getInt("userid"));
    	user.setUsername(userJSON.getString("username"));
    	user.setEmailid(userJSON.getString("emailid"));
    	user.setRole(new Role(2));
    	RandomStringGenerator stringGenerator = new RandomStringGenerator(WalletConstants.DEFAULT_PASSWORD_LENGTH);
    	String password = userJSON.has("password") ? userJSON.getString("password") : stringGenerator.generateString();
    	user.setPassword(password);
    	this.save(user);
    }
    

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String deleteUserDetails(String userData) throws WalletException {
    	User user = this.entityManager.find(User.class, userJSON.getInt("userid"));
    	this.delete(user);
    }
}
