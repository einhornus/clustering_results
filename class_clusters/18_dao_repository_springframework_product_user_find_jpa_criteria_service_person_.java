package com.companyname.backend.repositories.common;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommonJPARepository<T, ID extends Serializable> extends JpaRepository<T, ID>,
JpaSpecificationExecutor<T> {

}
--------------------

/**
 * Defines contracts for JDBC REF_CURSOR support.
 */
package org.hibernate.engine.jdbc.cursor.spi;

--------------------

package com.pavliuchyn.mynote.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pavliuchyn.mynote.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByLogin(String login);

}

--------------------

