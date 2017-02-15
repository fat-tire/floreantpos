package com.floreantpos.model.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseDeliveryAddressDAO extends com.floreantpos.model.dao._RootDAO {

	// query name references


	public static DeliveryAddressDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static DeliveryAddressDAO getInstance () {
		if (null == instance) instance = new DeliveryAddressDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return com.floreantpos.model.DeliveryAddress.class;
	}

    public Order getDefaultOrder () {
		return null;
    }

	/**
	 * Cast the object as a com.floreantpos.model.DeliveryAddress
	 */
	public com.floreantpos.model.DeliveryAddress cast (Object object) {
		return (com.floreantpos.model.DeliveryAddress) object;
	}

	public com.floreantpos.model.DeliveryAddress get(java.lang.Integer key)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.DeliveryAddress) get(getReferenceClass(), key);
	}

	public com.floreantpos.model.DeliveryAddress get(java.lang.Integer key, Session s)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.DeliveryAddress) get(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.DeliveryAddress load(java.lang.Integer key)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.DeliveryAddress) load(getReferenceClass(), key);
	}

	public com.floreantpos.model.DeliveryAddress load(java.lang.Integer key, Session s)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.DeliveryAddress) load(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.DeliveryAddress loadInitialize(java.lang.Integer key, Session s) 
			throws org.hibernate.HibernateException { 
		com.floreantpos.model.DeliveryAddress obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.DeliveryAddress> findAll () {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.DeliveryAddress> findAll (Order defaultOrder) {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List<com.floreantpos.model.DeliveryAddress> findAll (Session s, Order defaultOrder) {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param deliveryAddress a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.DeliveryAddress deliveryAddress)
		throws org.hibernate.HibernateException {
		return (java.lang.Integer) super.save(deliveryAddress);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param deliveryAddress a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.DeliveryAddress deliveryAddress, Session s)
		throws org.hibernate.HibernateException {
		return (java.lang.Integer) save((Object) deliveryAddress, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param deliveryAddress a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(com.floreantpos.model.DeliveryAddress deliveryAddress)
		throws org.hibernate.HibernateException {
		saveOrUpdate((Object) deliveryAddress);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param deliveryAddress a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(com.floreantpos.model.DeliveryAddress deliveryAddress, Session s)
		throws org.hibernate.HibernateException {
		saveOrUpdate((Object) deliveryAddress, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param deliveryAddress a transient instance containing updated state
	 */
	public void update(com.floreantpos.model.DeliveryAddress deliveryAddress) 
		throws org.hibernate.HibernateException {
		update((Object) deliveryAddress);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param deliveryAddress a transient instance containing updated state
	 * @param the Session
	 */
	public void update(com.floreantpos.model.DeliveryAddress deliveryAddress, Session s)
		throws org.hibernate.HibernateException {
		update((Object) deliveryAddress, s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(java.lang.Integer id)
		throws org.hibernate.HibernateException {
		delete((Object) load(id));
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param id the instance ID to be removed
	 * @param s the Session
	 */
	public void delete(java.lang.Integer id, Session s)
		throws org.hibernate.HibernateException {
		delete((Object) load(id, s), s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param deliveryAddress the instance to be removed
	 */
	public void delete(com.floreantpos.model.DeliveryAddress deliveryAddress)
		throws org.hibernate.HibernateException {
		delete((Object) deliveryAddress);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param deliveryAddress the instance to be removed
	 * @param s the Session
	 */
	public void delete(com.floreantpos.model.DeliveryAddress deliveryAddress, Session s)
		throws org.hibernate.HibernateException {
		delete((Object) deliveryAddress, s);
	}
	
	/**
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * For example 
	 * <ul> 
	 * <li>where a database trigger alters the object state upon insert or update</li>
	 * <li>after executing direct SQL (eg. a mass update) in the same session</li>
	 * <li>after inserting a Blob or Clob</li>
	 * </ul>
	 */
	public void refresh (com.floreantpos.model.DeliveryAddress deliveryAddress, Session s)
		throws org.hibernate.HibernateException {
		refresh((Object) deliveryAddress, s);
	}


}