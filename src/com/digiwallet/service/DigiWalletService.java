package com.digiwallet.service;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.digiwallet.dao.UserDaoActions;
import com.digiwallet.daoimpl.UserDaoImpl;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

@Path("/users")
public class DigiWalletService {
	@PersistenceContext
    private EntityManager entityManager;
	
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
		List users = (List) entityManager.createNamedQuery("User.findAll").getResultList();
		String output = "Welcome   : " + users.toString();
		System.out.println("Before printing");
		System.out.println("output " + output);
		return Response.status(200).entity(output).build();
 
	}
 
}
