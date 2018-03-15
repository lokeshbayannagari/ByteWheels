package com.bytewheels.daoimpl;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bytewheels.dao.RoleDao;
import com.bytewheels.entity.Role;

@Dependent
public class RoleDaoImpl implements RoleDao {
	@PersistenceContext
	EntityManager entityManager;

	@Override	
	public Role getRole(Integer roleid) {
		try {
			Role role = entityManager.find(Role.class, roleid);
			return role;
		} catch(Exception e) {
			throw e;
		}
	}
}
