package com.digiwallet.daoimpl;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digiwallet.dao.UserDaoActions;
import com.digiwallet.entity.Card;
import com.digiwallet.entity.Role;
import com.digiwallet.entity.User;
import com.digiwallet.exception.WalletException;
import com.digiwallet.utils.RandomStringGenerator;
import com.digiwallet.utils.WalletConstants;
import com.digiwallet.utils.WalletEncryptor;
import com.digiwallet.utils.WalletLogger;

@Dependent
public class UserDaoImpl implements UserDaoActions
{
    WalletLogger logger = new WalletLogger(UserDaoImpl.class);
     WalletEncryptor sha1 = new WalletEncryptor();        
    
    @PersistenceContext
    private EntityManager entityManager;    
    
    @Inject
    RoleDao roleDao;

    @Transactional
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
    public String getUserCardDetails() throws WalletException 
    {
        
        final String METHOD_NAME = "getUserAndHisCardData";
	logger.logEntering(METHOD_NAME);
        JSONObject responseObject = new JSONObject();
    	try 
        {    		
            JSONArray users = new JSONArray();
            List<User> userList = this.entityManager.createNamedQuery("User.findAll").getResultList();
            if(userList.isEmpty())
            {
                responseObject.put("err", true);
                responseObject.put("message", "No users found.");
            }
            else
            {
                for(User user : userList)
                {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("userid", user.getUserid());
                    userJSON.put("username", user.getUsername());
                    userJSON.put("emailid", user.getEmailid());
                    userJSON.put("roleid", user.getRole().getRoleid());

                    JSONArray cards = new JSONArray();
                    for(Card card : user.getCards())
                    {
                        JSONObject cardJSON = new JSONObject();
                        cardJSON.put("cardid", card.getCardid());
                        cardJSON.put("userid", card.getUser().getUserid());
                        cardJSON.put("cardtypeid", card.getCardtype().getCardtypeid());
                        cardJSON.put("expirydate", card.getExpirydate());
                        cardJSON.put("cardnumber", card.getCardnumber());
                        cards.put(cardJSON);
                    }
                    userJSON.put("cards", cards);
                    users.put(userJSON);
                }
                responseObject.put("err", false);
                responseObject.put("data", users.toString()); 
            }
            
        } 
        catch (JSONException e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
        } 
        catch (Exception e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
        }
        finally
	{
	    logger.logExiting(METHOD_NAME);
	}
      return  responseObject.toString();
    }

    @Override
    @Transactional
    public String addUserDetails(String userData) throws WalletException 
    {
        JSONObject responseObject = new JSONObject();
        final String METHOD_NAME = "getUserToDatabase";
	logger.logEntering(METHOD_NAME, "userData : " + userData);
        try 
        {
//            check whether user has admin role to add user code
//            	    if( ! commonManager.isLoggedInUserAuthorisedForAction(UserPrivilegeConstants.UserAction.DELETE_USER))
//	    {
//		logger.logError(METHOD_NAME, "User : " + oLoggedInUser.getUserID() + " does not have permission to delete users.");
//		throw new KwizolException(KwizolException.Codes.CODE_NO_PERMISSION);
//	    }
            
            
            JSONObject userJSON = new JSONObject(userData);
            User user = new User();
            user.setUsername(userJSON.getString("username"));
            user.setEmailid(userJSON.getString("emailid"));            
            user.setRole(roleDao.getRole(2));
            RandomStringGenerator stringGenerator = new RandomStringGenerator(WalletConstants.DEFAULT_PASSWORD_LENGTH);
            String password = userJSON.has("password") ? userJSON.getString("password") : stringGenerator.generateString();
            user.setPassword(sha1.getEncryptedString(password, WalletConstants.SHA_ALGORITHM));
            entityManager.persist(user);
            entityManager.flush();
            responseObject.put("err",false);
            responseObject.put("message","succesfully added user.");
        } 
        catch (JSONException e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
        } 
        catch (Exception e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
        }
        finally
	{
	    logger.logExiting(METHOD_NAME);
	}
        return responseObject.toString();
    }

    @Transactional
    public String signUpUser(String userData) throws WalletException 
    {
        final String METHOD_NAME = "getUserToDatabase";
	logger.logEntering(METHOD_NAME, "userData : " + userData);
        JSONObject responseObject = new JSONObject();
        try 
        {
            JSONObject userJSON = new JSONObject(userData);
            String emailID = userJSON.getString("emailid");
            if(isEmailIDPresentInSystem(emailID))
            {
                throw new WalletException(WalletException.Codes.CODE_USERS_DUPLICATE_EMAILID);
            }
            else
            {
                User user = new User();
                user.setUsername(userJSON.getString("username"));
                user.setEmailid(emailID); 
                user.setRole(roleDao.getRole(1));
                RandomStringGenerator stringGenerator = new RandomStringGenerator(WalletConstants.DEFAULT_PASSWORD_LENGTH);
                String password = userJSON.has("password") ? userJSON.getString("password") : stringGenerator.generateString();
                user.setPassword(sha1.getEncryptedString(password, WalletConstants.SHA_ALGORITHM));
                entityManager.persist(user);
                entityManager.flush();
                responseObject.put("err", false);
                responseObject.put("message","succesffuly added user");
            }
           
        } catch (JSONException e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
        } 
        catch (Exception e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
        }
        finally
	{
	    logger.logExiting(METHOD_NAME);
	}
        return  responseObject.toString();
    }

    
    @Override
    @Transactional
    public String updateUserDetails(Integer userid, String userData) throws WalletException 
    {
        final String METHOD_NAME = "updateUserDetails";
	logger.logEntering(METHOD_NAME, "userData : " + userData);
        JSONObject responseObject = new JSONObject();
        try 
        {
            JSONObject userJSON = new JSONObject(userData);
            User user = this.entityManager.find(User.class, userid);
            if(user == null)
            {
                throw new WalletException(WalletException.Codes.CODE_NULL_ENTITY);
            }
            else
            {
                user.setUsername(userJSON.getString("username"));
                user.setEmailid(userJSON.getString("emailid"));                
                RandomStringGenerator stringGenerator = new RandomStringGenerator(WalletConstants.DEFAULT_PASSWORD_LENGTH);
                String password = userJSON.has("password") ? userJSON.getString("password") : stringGenerator.generateString();
                user.setPassword(sha1.getEncryptedString(password, WalletConstants.SHA_ALGORITHM));
                entityManager.persist(user);
                entityManager.flush();
                responseObject.put("err", false);
                responseObject.put("message","succesffuly updated user");
            }
           
        } catch (JSONException e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
        } 
        catch (Exception e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
        }
        finally
	{
	    logger.logExiting(METHOD_NAME);
	}
        return responseObject.toString();
    }
    

    @Override
    @Transactional
    public String deleteUserDetails(Integer userid) throws WalletException 
    {
        final String METHOD_NAME = "DeteleUserDetails";
        logger.logEntering(METHOD_NAME, "userData : " );
        JSONObject responseObject = new JSONObject();
        
        try 
        {
            User user = this.entityManager.find(User.class, userid);
            if(user == null)
            {
                throw new WalletException(WalletException.Codes.CODE_USERS_FAILED_TO_DELETE);
            }
            else
            {
            	
                entityManager.remove(user);
                responseObject.put("err", false);
                responseObject.put("message","succesffuly deleted user");
            }
        }
        catch (JSONException e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
        } 
        catch (Exception e) 
        {
             logger.logError(null, e);
            throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
        }
        finally
	{
	    logger.logExiting(METHOD_NAME);
	}
        return responseObject.toString();
    }
    
    private boolean isEmailIDPresentInSystem(String strEmailID)
    {
	final String METHOD_NAME = "isEmailIDPresentInSystem";
	logger.logEntering(METHOD_NAME);
	try
	{
	    String strData = this.entityManager.createNativeQuery("select count(*) from users uo where uo.emailid = ?")
		.setParameter(1, strEmailID)
		.getSingleResult()
		.toString();
	    return Integer.parseInt(strData) > 0;
	}
	catch(Exception ex)
	{
	    return false;
	}
    }

	@Override
	public String getUserDetails(Integer userid) throws WalletException {
		try {
			JSONObject responseObject = new JSONObject();
            User user = this.entityManager.find(User.class, userid);
            if(user == null)
            {
                throw new WalletException(WalletException.Codes.CODE_NULL_ENTITY);
            }
            else
            {
            	JSONObject userJSON = new JSONObject();
                userJSON.put("userid", user.getUserid());
                userJSON.put("username", user.getUsername());
                userJSON.put("emailid", user.getEmailid());
                userJSON.put("roleid", user.getRole().getRoleid());

                JSONArray cards = new JSONArray();
                for(Card card : user.getCards())
                {
                    JSONObject cardJSON = new JSONObject();
                    cardJSON.put("cardid", card.getCardid());
                    cardJSON.put("userid", card.getUser().getUserid());
                    cardJSON.put("cardtypeid", card.getCardtype().getCardtypeid());
                    cardJSON.put("expirydate", card.getExpirydate());
                    cardJSON.put("cardnumber", card.getCardnumber());
                    cards.put(cardJSON);
                }
                userJSON.put("cards", cards);
                responseObject.put("err", false);
                responseObject.put("data", userJSON.toString()); 
            }
            return responseObject.toString();
		} catch(WalletException e) {
			throw e;
		} catch (Exception e) {
			throw new WalletException(WalletException.Codes.CODE_NULL_ENTITY);
		}
	}
}

