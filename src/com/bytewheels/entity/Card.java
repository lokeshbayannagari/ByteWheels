package com.bytewheels.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the cards database table.
 * 
 */
@Entity
@Table(name="cards")
@NamedQuery(name="Card.findAll", query="SELECT c FROM Card c")
public class Card implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int cardid;

	private BigInteger cardnumber;

	@Temporal(TemporalType.DATE)
	private Date expirydate;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;

	//bi-directional many-to-one association to Cardtype
	@ManyToOne
	@JoinColumn(name="cardtypeid")
	private Cardtype cardtype;

	public Card() {
	}

	public int getCardid() {
		return this.cardid;
	}

	public void setCardid(int cardid) {
		this.cardid = cardid;
	}

	public BigInteger getCardnumber() {
		return this.cardnumber;
	}

	public void setCardnumber(BigInteger cardnumber) {
		this.cardnumber = cardnumber;
	}

	public Date getExpirydate() {
		return this.expirydate;
	}

	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Cardtype getCardtype() {
		return this.cardtype;
	}

	public void setCardtype(Cardtype cardtype) {
		this.cardtype = cardtype;
	}

}