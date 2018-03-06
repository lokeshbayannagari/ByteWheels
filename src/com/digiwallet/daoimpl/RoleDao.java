package com.digiwallet.daoimpl;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.digiwallet.entity.Role;

@Dependent
public class RoleDao {
	@PersistenceContext
	EntityManager entityManager;
	
	public Role getRole(Integer roleid) {
		try {
			Role role = entityManager.find(Role.class, roleid);
			return role;
		} catch(Exception e) {
			throw e;
		}
	}
}
