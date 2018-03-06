package com.digiwallet.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;

	private String emailid;

	private String password;

	private String username;

	//bi-directional many-to-one association to Card
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
	private List<Card> cards;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="roleid")
	private Role role;

	public User() {
	}

	public int getUserid() {
		return this.userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getEmailid() {
		return this.emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Card> getCards() {
		return this.cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public Card addCard(Card card) {
		getCards().add(card);
		card.setUser(this);

		return card;
	}

	public Card removeCard(Card card) {
		getCards().remove(card);
		card.setUser(null);

		return card;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [userid=" + userid + ", emailid=" + emailid
				+ ", password=" + password + ", username=" + username
				+ ", cards=" + cards + ", role=" + role + "]";
	}

}