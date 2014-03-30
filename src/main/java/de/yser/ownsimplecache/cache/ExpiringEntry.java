/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.yser.ownsimplecache.cache;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author MacYser
 */
public class ExpiringEntry<V> {

	private V value;
	private final Date created;
	private final long expiringTime;

	public ExpiringEntry(V value, long expiringTime) {
		this.value = value;
		this.created = new Date();
		this.expiringTime = expiringTime;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public boolean isExpiried() {
		if (expiringTime < 0) {
			return false;
		} else {
			return (created.getTime() + expiringTime) < new Date().getTime();
		}

	}

	public Date getCreated() {
		return created;
	}

	public long getExpiringTime() {
		return expiringTime;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + Objects.hashCode(this.value);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ExpiringEntry<?> other = (ExpiringEntry<?>) obj;
		return Objects.equals(this.value, other.value);
	}

}
