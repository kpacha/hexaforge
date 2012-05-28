package com.hexaforge.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static EntityManagerFactory emfInstance = null;

	public static EntityManagerFactory get() {
		if (emfInstance == null)
			emfInstance = Persistence
					.createEntityManagerFactory("transactions-optional");
		return emfInstance;
	}

	public static EntityManager getEntityManager() {
		return get().createEntityManager();
	}
}
