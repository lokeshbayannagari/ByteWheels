/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiwallet.daoimpl;

import com.digiwallet.dao.CardDaoActions;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import com.digiwallet.entity.Card;
import com.digiwallet.entity.Cardtype;
import com.digiwallet.entity.User;
import com.digiwallet.exception.WalletException;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author lokesh
 */

@Stateless
public class CardDaoImpl implements CardDaoActions {

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
	
	/*
{
    "cards": [
        { "cardid": "1", "userid": "100", "cardtypeid": "2", "expirydate":"20/10/2010", "cardnumber":"12345" },
        { "cardid": "2", "userid": "100", "cardtypeid": "2", "expirydate":"02/01/2020", "cardnumber":"67890" } 
    ]
} 
	 */
	@Override
	public String getUserCardDetails(int userID) throws WalletException {
		String strResult = null;

		User objUser = getUser(userID);
		List<Card> cards = objUser.getCards();

		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		try {
			for (Card card : cards) {
				JSONObject cardJson = new JSONObject();
				cardJson.put("cardid", card.getCardid());
				cardJson.put("userid", userID);
				cardJson.put("cardtypeid", card.getCardtype().getCardtypeid());
				cardJson.put("expirydate", card.getExpirydate());
				cardJson.put("cardnumber", card.getCardnumber());
				jsonArray.put(cardJson);
				responseDetailsJson.put("cards", jsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		strResult = responseDetailsJson.toString();
		return strResult;
	}

	/*
{
    "cards": [
        { "cardid": "1", "userid": "100", "cardtypeid": "2", "expirydate":"20/10/2010", "cardnumber":"12345" },
        { "cardid": "2", "userid": "100", "cardtypeid": "2", "expirydate":"02/01/2020", "cardnumber":"67890" } 
    ]
} 
	 */
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String addCardDetails(String userCardData) throws WalletException {
		String strResult = null;
		try {
			JSONObject cardsJson = new JSONObject(userCardData);
			JSONArray jsonArray = cardsJson.getJSONArray("cards");
			
			for(int index = 0; index < jsonArray.length(); index++)
			{
				JSONObject cardJson = jsonArray.getJSONObject(index);
				Card objCard = new Card();
				objCard.setCardid(cardJson.getInt("cardid"));
				objCard.setCardnumber(cardJson.getInt("cardnumber"));
				
				objCard.setCardtype(getCardType(cardJson.getInt("cardtypeid")));
				objCard.setExpirydate(new Date(cardJson.getString("expirydate")));
				User objUser = getUser(cardJson.getInt("userid"));
				objCard.setUser(objUser);
				objUser.addCard(objCard);
				//FIXME persist
				this.save(objCard);
				this.save(objUser);
			}

			strResult = "success";
			
		} catch (JSONException e) {
			strResult = "error";
			e.printStackTrace();
		}
		return strResult;
	}
/*
	{ "cardid": "1", "userid": "100", "cardtypeid": "2", "expirydate":"20/10/2010", "cardnumber":"12345" }
*/
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String updateCardDetails(String userCardData) throws WalletException {
		String strResult = null;
		
		try {
			JSONObject cardJson = new JSONObject(userCardData);
			Card objCard = getCard(cardJson.getInt("cardid"));
			objCard.setCardnumber(cardJson.getInt("cardnumber"));
			objCard.setCardtype(getCardType(cardJson.getInt("cardid")));
			objCard.setExpirydate(new Date(cardJson.getString("expirydate")));
			this.save(objCard);
			strResult = "success";
		} catch (JSONException e) {
			strResult = "error";
			e.printStackTrace();
		}
		return strResult;
	}

/*
	{ "cardid": "1", "userid": "100"}
*/
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String deleteCardDetails(String userCardData) throws WalletException {
		String strResult = null;
		
		try {
			JSONObject cardJson = new JSONObject(userCardData);
			User objUser = getUser(cardJson.getInt("userid"));
			Card objCard = getCard(cardJson.getInt("cardid"));
			objUser.removeCard(objCard);
			this.delete(objCard);
			this.save(objUser);
			strResult = "success";
		} catch (JSONException e) {
			strResult = "error";
			e.printStackTrace();
		}
		
		return strResult;
	}
	
	private Cardtype getCardType(int cardTypeID) {
		Cardtype objCardType = entityManager.find(Cardtype.class, cardTypeID);
		return objCardType;
	}

	private User getUser(int userID) {
		User objUser = entityManager.find(User.class, userID);
		return objUser;
	}
	
	private Card getCard(int cardID) {
		Card objCard = entityManager.find(Card.class, cardID);
		return objCard;
	}
}