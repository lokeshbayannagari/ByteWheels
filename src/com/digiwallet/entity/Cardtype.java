package com.digiwallet.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the cardtypes database table.
 * 
 */
@Entity
@Table(name="cardtypes")
@NamedQuery(name="Cardtype.findAll", query="SELECT c FROM Cardtype c")
public class Cardtype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int cardtypeid;

	private String cardtype;

	//bi-directional many-to-one association to Card
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cardtype")
	private List<Card> cards;

	public Cardtype() {
	}

	public int getCardtypeid() {
		return this.cardtypeid;
	}

	public void setCardtypeid(int cardtypeid) {
		this.cardtypeid = cardtypeid;
	}

	public String getCardtype() {
		return this.cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public List<Card> getCards() {
		return this.cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public Card addCard(Card card) {
		getCards().add(card);
		card.setCardtype(this);

		return card;
	}

	public Card removeCard(Card card) {
		getCards().remove(card);
		card.setCardtype(null);

		return card;
	}

}