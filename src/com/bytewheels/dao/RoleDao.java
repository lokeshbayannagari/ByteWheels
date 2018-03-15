package com.bytewheels.dao;

import com.bytewheels.entity.Role;
import com.bytewheels.exception.ByteWheelsException;

/**
 *
 * @author lokesh
 */

public interface RoleDao 
{
    public Role getRole(Integer roleid) throws ByteWheelsException;
}
