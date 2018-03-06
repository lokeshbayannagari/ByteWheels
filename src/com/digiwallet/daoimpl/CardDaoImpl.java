/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digiwallet.daoimpl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digiwallet.dao.CardDaoActions;
import com.digiwallet.entity.Card;
import com.digiwallet.entity.Cardtype;
import com.digiwallet.entity.User;
import com.digiwallet.exception.WalletException;
import com.digiwallet.utils.WalletLogger;

/**
 *
 * @author lokesh
 */

@Stateless
public class CardDaoImpl implements CardDaoActions {

	WalletLogger logger = new WalletLogger(CardDaoImpl.class);
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void save(Object obj) {
		this.entityManager.persist(obj);
		this.entityManager.flush();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void delete(Object obj) {
		this.entityManager.remove(obj);
		this.entityManager.flush();
	}

	/*
	 * { "cards": [ { "cardid": "1", "userid": "100", "cardtypeid": "2",
	 * "expirydate":"20/10/2010", "cardnumber":"12345" }, { "cardid": "2", "userid":
	 * "100", "cardtypeid": "2", "expirydate":"02/01/2020", "cardnumber":"67890" } ]
	 * }
	 */
	@Override
	public String getUserCardDetails(int userID) throws WalletException {
		final String METHOD_NAME = "getUserAndHisCardData";
		logger.logEntering(METHOD_NAME, "userID : " + userID);
		JSONObject responseObject = new JSONObject();
		try {
			User objUser = getUser(userID);
			List<Card> cards = objUser.getCards();
			if (cards.isEmpty()) {
				responseObject.put("err", true);
				responseObject.put("message", "No cards found.");
			} else {
				JSONObject responseDetailsJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();

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
				responseObject.put("err", false);
				responseObject.put("data", responseDetailsJson.toString());
			}

		} catch (JSONException e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
		} catch (Exception e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
		} finally {
			logger.logExiting(METHOD_NAME);
		}

		return responseObject.toString();
	}

	/*
	 * 
	 * { "cardid": "1", "userid": "100", "cardtypeid": "2",
	 * "expirydate":"20/10/2010", "cardnumber":"12345" }
	 * 
	 */
	@Override
	@Transactional
	public String addCardDetails(String userCardData) throws WalletException {
		JSONObject responseObject = new JSONObject();
		final String METHOD_NAME = "addCardToUser";
		logger.logEntering(METHOD_NAME, "userCardData : " + userCardData);
		try {
			JSONObject cardJson = new JSONObject(userCardData);
			User objUser = getUser(cardJson.getInt("userid"));
			if (objUser == null) {
				throw new WalletException(WalletException.Codes.CODE_NULL_ENTITY);
			} else {
				Card objCard = new Card();
				objCard.setCardnumber(BigInteger.valueOf(cardJson.getLong("cardnumber")));

				objCard.setCardtype(getCardType(cardJson.getInt("cardtypeid")));
				objCard.setExpirydate(new Date(cardJson.getString("expirydate")));

				objCard.setUser(objUser);
				objUser.addCard(objCard);

				entityManager.persist(objCard);
				entityManager.persist(objUser);
				entityManager.flush();
				responseObject.put("err", false);
				responseObject.put("message", "successfully added card");
			}

		} catch (JSONException e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
		} catch (Exception e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
		} finally {
			logger.logExiting(METHOD_NAME);
		}
		return responseObject.toString();
	}

	/*
	 * { "cardid": "1", "userid": "100", "cardtypeid": "2",
	 * "expirydate":"20/10/2010", "cardnumber":"12345" }
	 */
	@Override
	@Transactional
	public String updateCardDetails(Integer cardid, String userCardData) throws WalletException {
		JSONObject responseObject = new JSONObject();
		final String METHOD_NAME = "UpdateCardDetails";
		logger.logEntering(METHOD_NAME, "userCardData : " + userCardData);

		try {
			JSONObject cardJson = new JSONObject(userCardData);
			Card objCard = getCard(cardid);
			String cardNumber = cardJson.getString("cardnumber");
			objCard.setCardnumber(BigInteger.valueOf(Long.parseLong(cardNumber)));			
			System.out.println(objCard.getCardnumber());
			objCard.setExpirydate(new Date(cardJson.getString("expirydate")));
			entityManager.persist(objCard);
			entityManager.flush();
			responseObject.put("err", false);
			responseObject.put("message", "succesffuly updated card");
		} catch (JSONException e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
		} catch (Exception e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
		} finally {
			logger.logExiting(METHOD_NAME);
		}
		return responseObject.toString();
	}

	/*
	 * { "cardid": "1", "userid": "100"}
	 */
	@Override
	@Transactional
	public String deleteCardDetails(Integer userId, Integer cardId) throws WalletException {
		JSONObject responseObject = new JSONObject();
		final String METHOD_NAME = "DeleteCardDetails";

		try {
			User objUser = getUser(userId);
			Card objCard = getCard(cardId);			
			entityManager.remove(objCard);
			entityManager.flush();			
			responseObject.put("err", false);
			responseObject.put("message", "succesffuly deleted card ");

		} catch (JSONException e) {
			logger.logError(null, e);
			throw new WalletException(WalletException.Codes.CODE_BAD_JSON_STRING);
		} catch (Exception e) {
			logger.logError(null, e);
			e.printStackTrace();
			throw new WalletException(WalletException.Codes.CODE_INTERNAL_SYSTEM_ERROR);
		} finally {
			logger.logExiting(METHOD_NAME);
		}
		return responseObject.toString();
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