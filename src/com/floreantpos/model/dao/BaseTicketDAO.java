package com.floreantpos.model.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import com.floreantpos.model.dao.TicketDAO;
import org.hibernate.criterion.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseTicketDAO extends com.floreantpos.model.dao._RootDAO {

	// query name references


	public static TicketDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static TicketDAO getInstance () {
		if (null == instance) instance = new TicketDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return com.floreantpos.model.Ticket.class;
	}

    public Order getDefaultOrder () {
		return null;
    }

	/**
	 * Cast the object as a com.floreantpos.model.Ticket
	 */
	public com.floreantpos.model.Ticket cast (Object object) {
		return (com.floreantpos.model.Ticket) object;
	}

	public com.floreantpos.model.Ticket get(java.lang.Integer key)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.Ticket) get(getReferenceClass(), key);
	}

	public com.floreantpos.model.Ticket get(java.lang.Integer key, Session s)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.Ticket) get(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.Ticket load(java.lang.Integer key)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.Ticket) load(getReferenceClass(), key);
	}

	public com.floreantpos.model.Ticket load(java.lang.Integer key, Session s)
		throws org.hibernate.HibernateException {
		return (com.floreantpos.model.Ticket) load(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.Ticket loadInitialize(java.lang.Integer key, Session s) 
			throws org.hibernate.HibernateException { 
		com.floreantpos.model.Ticket obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.Ticket> findAll () {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.Ticket> findAll (Order defaultOrder) {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List<com.floreantpos.model.Ticket> findAll (Session s, Order defaultOrder) {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param ticket a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.Ticket ticket)
		throws org.hibernate.HibernateException {
		return (java.lang.Integer) super.save(ticket);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param ticket a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.Ticket ticket, Session s)
		throws org.hibernate.HibernateException {
		return (java.lang.Integer) save((Object) ticket, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param ticket a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(com.floreantpos.model.Ticket ticket)
		throws org.hibernate.HibernateException {
		saveOrUpdate((Object) ticket);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param ticket a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(com.floreantpos.model.Ticket ticket, Session s)
		throws org.hibernate.HibernateException {
		saveOrUpdate((Object) ticket, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param ticket a transient instance containing updated state
	 */
	public void update(com.floreantpos.model.Ticket ticket) 
		throws org.hibernate.HibernateException {
		update((Object) ticket);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param ticket a transient instance containing updated state
	 * @param the Session
	 */
	public void update(com.floreantpos.model.Ticket ticket, Session s)
		throws org.hibernate.HibernateException {
		update((Object) ticket, s);
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
	 * @param ticket the instance to be removed
	 */
	public void delete(com.floreantpos.model.Ticket ticket)
		throws org.hibernate.HibernateException {
		delete((Object) ticket);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param ticket the instance to be removed
	 * @param s the Session
	 */
	public void delete(com.floreantpos.model.Ticket ticket, Session s)
		throws org.hibernate.HibernateException {
		delete((Object) ticket, s);
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
	public void refresh (com.floreantpos.model.Ticket ticket, Session s)
		throws org.hibernate.HibernateException {
		refresh((Object) ticket, s);
	}


}