package com.digiwallet.service;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.digiwallet.dao.UserDaoActions;
import com.digiwallet.exception.WalletException;

@Path("/users")
public class DigiWalletService {
	
	@EJB
	UserDaoActions userDaoActions;
 
	@GET
	@Path("/{id}")
	public Response getMsg(@PathParam("name") String name) {
 
		String output = "Welcome   : " + name;
 
		return Response.status(200).entity(output).build();
 
	}
	
	
	@GET
	@Path("/")
	public Response getDefault() {
		System.out.println("users : "+userDaoActions);
		String output;
		try {
			output = "Welcome   : " + userDaoActions.getUserCardDetails();
		} catch (WalletException e) {
			output = e.getInternalMessage();
		}
		System.out.println("Before printing");
		System.out.println("output " + output);
		return Response.status(200).entity(output).build();
 
	}
 
}
