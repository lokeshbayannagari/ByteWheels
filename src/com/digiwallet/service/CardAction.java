package com.digiwallet.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digiwallet.dao.CardDaoActions;
import com.digiwallet.exception.WalletException;

@Path("/users/{userid}/cards")
@RequestScoped
public class CardAction {
	
	@Inject
	CardDaoActions cardDao;
	
	@PathParam ("userid")
	Integer userId;
	
	@GET
	@Path("/")
	public Response getDefault() {
		String output;
		try {
			output = "Welcome   : " + cardDao.getUserCardDetails(userId);
		} catch (WalletException e) {
			output = e.getUserMessage();
		}
		return Response.status(200).entity(output).build();
 
	}
	
	@DELETE
	@Path("/{cardId}")
	public Response delete(@PathParam("cardId")Integer cardId) {
		String output;
		try {
			output = "Welcome   : " + cardDao.deleteCardDetails(userId, cardId);
		} catch (WalletException e) {
			output = e.getUserMessage();
		}
		System.out.println("Before printing");
		System.out.println("output " + output);
		return Response.status(200).entity(output).build();
 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response addUser(String json) {
		try {
			String output;
			try {
				output = cardDao.addCardDetails(json);
			} catch (WalletException e) {
				output = e.getUserMessage();
			}
			return Response.status(200).entity(output).build();
		} catch (Exception e) {
			return Response.status(200).entity("{\"err\":true}").build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateUser(String json, @PathParam("id") Integer id) {
		try {
			String output;
			try {
				output = cardDao.updateCardDetails(id, json);
			} catch (WalletException e) {
				output = e.getUserMessage();
			}
			return Response.status(200).entity(output).build();
		} catch (Exception e) {
			return Response.status(200).entity("{\"err\":true}").build();
		}
	}	
}
