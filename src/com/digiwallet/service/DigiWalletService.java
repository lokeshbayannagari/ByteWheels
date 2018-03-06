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

import com.digiwallet.dao.UserDaoActions;
import com.digiwallet.exception.WalletException;

@Path("/users")
@RequestScoped
public class DigiWalletService {
	
	@Inject
	UserDaoActions userDaoActions;
	
	@GET
	@Path("/")
	public Response getDefault() {
		System.out.println("users : "+userDaoActions);
		String output;
		try {
			output = "Welcome   : " + userDaoActions.getUserCardDetails();
		} catch (WalletException e) {
			output = e.getUserMessage();
		}
		return Response.status(200).entity(output).build();
 
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id")Integer id) {
		System.out.println("users : "+userDaoActions);
		String output;
		try {
			output = "Welcome   : " + userDaoActions.deleteUserDetails(id);
		} catch (WalletException e) {
			output = e.getUserMessage();
		}
		System.out.println("Before printing");
		System.out.println("output " + output);
		return Response.status(200).entity(output).build();
 
	}
	
	@POST 
	@Consumes("application/json")
	@Path("/signup") 
	public Response signUp(String json) {		
		String output;
		try {
			output = userDaoActions.signUpUser(json.toString());
		} catch (WalletException e) {
			output = e.getUserMessage();
		}
		return Response.status(200).entity(output).build(); 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response addUser(String json) {
		try {
			String output;
			try {
				output = userDaoActions.addUserDetails(json.toString());
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
				output = userDaoActions.updateUserDetails(id, json.toString());
			} catch (WalletException e) {
				output = e.getUserMessage();
			}
			return Response.status(200).entity(output).build();
		} catch (Exception e) {
			return Response.status(200).entity("{\"err\":true}").build();
		}
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getUser(@PathParam("id") Integer id) {
		try {
			String output;
			try {
				output = userDaoActions.getUserDetails(id);
			} catch (WalletException e) {
				output = e.getUserMessage();
			}
			return Response.status(200).entity(output).build();
		} catch (Exception e) {
			return Response.status(200).entity("{\"err\":true}").build();
		}
	}
 
}
