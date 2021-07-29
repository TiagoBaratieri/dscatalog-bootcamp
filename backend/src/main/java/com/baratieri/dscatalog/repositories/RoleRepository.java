package com.baratieri.dscatalog.repositories;

import com.baratieri.dscatalog.entities.Role;
import com.baratieri.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
