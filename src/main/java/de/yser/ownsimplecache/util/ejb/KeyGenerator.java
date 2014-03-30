/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.yser.ownsimplecache.util.ejb;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MacYser
 */
public class KeyGenerator {

	private static final Logger LOG = Logger.getLogger(KeyGenerator.class.getName());

	public KeyGenerator() {
	}

	String generateKey(Method method, Object[] params) {
		String generatedKey = method.getName();
		LOG.log(Level.INFO, "CACHE: -					Generated key is {0}", new Object[]{generatedKey});
		return generatedKey;
	}

}
