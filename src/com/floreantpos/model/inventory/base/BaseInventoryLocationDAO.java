package com.floreantpos.model.inventory.base;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import com.floreantpos.model.inventory.dao.InventoryLocationDAO;
import org.hibernate.criterion.Order;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseInventoryLocationDAO extends com.floreantpos.model.inventory.dao._RootDAO {

	// query name references


	public static InventoryLocationDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static InventoryLocationDAO getInstance () {
		if (null == instance) instance = new InventoryLocationDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return com.floreantpos.model.inventory.InventoryLocation.class;
	}

    public Order getDefaultOrder () {
		return Order.asc("name");
    }

	/**
	 * Cast the object as a com.floreantpos.model.inventory.InventoryLocation
	 */
	public com.floreantpos.model.inventory.InventoryLocation cast (Object object) {
		return (com.floreantpos.model.inventory.InventoryLocation) object;
	}

	public com.floreantpos.model.inventory.InventoryLocation get(java.lang.Integer key)
	{
		return (com.floreantpos.model.inventory.InventoryLocation) get(getReferenceClass(), key);
	}

	public com.floreantpos.model.inventory.InventoryLocation get(java.lang.Integer key, Session s)
	{
		return (com.floreantpos.model.inventory.InventoryLocation) get(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.inventory.InventoryLocation load(java.lang.Integer key)
	{
		return (com.floreantpos.model.inventory.InventoryLocation) load(getReferenceClass(), key);
	}

	public com.floreantpos.model.inventory.InventoryLocation load(java.lang.Integer key, Session s)
	{
		return (com.floreantpos.model.inventory.InventoryLocation) load(getReferenceClass(), key, s);
	}

	public com.floreantpos.model.inventory.InventoryLocation loadInitialize(java.lang.Integer key, Session s) 
	{ 
		com.floreantpos.model.inventory.InventoryLocation obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.inventory.InventoryLocation> findAll () {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 */
	public java.util.List<com.floreantpos.model.inventory.InventoryLocation> findAll (Order defaultOrder) {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 */
	public java.util.List<com.floreantpos.model.inventory.InventoryLocation> findAll (Session s, Order defaultOrder) {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param inventoryLocation a transient instance of a persistent class 
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.inventory.InventoryLocation inventoryLocation)
	{
		return (java.lang.Integer) super.save(inventoryLocation);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param inventoryLocation a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 */
	public java.lang.Integer save(com.floreantpos.model.inventory.InventoryLocation inventoryLocation, Session s)
	{
		return (java.lang.Integer) save((Object) inventoryLocation, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param inventoryLocation a transient instance containing new or updated state 
	 */
	public void saveOrUpdate(com.floreantpos.model.inventory.InventoryLocation inventoryLocation)
	{
		saveOrUpdate((Object) inventoryLocation);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param inventoryLocation a transient instance containing new or updated state.
	 * @param s the Session.
	 */
	public void saveOrUpdate(com.floreantpos.model.inventory.InventoryLocation inventoryLocation, Session s)
	{
		saveOrUpdate((Object) inventoryLocation, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param inventoryLocation a transient instance containing updated state
	 */
	public void update(com.floreantpos.model.inventory.InventoryLocation inventoryLocation) 
	{
		update((Object) inventoryLocation);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param inventoryLocation a transient instance containing updated state
	 * @param the Session
	 */
	public void update(com.floreantpos.model.inventory.InventoryLocation inventoryLocation, Session s)
	{
		update((Object) inventoryLocation, s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 */
	public void delete(java.lang.Integer id)
	{
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
	{
		delete((Object) load(id, s), s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param inventoryLocation the instance to be removed
	 */
	public void delete(com.floreantpos.model.inventory.InventoryLocation inventoryLocation)
	{
		delete((Object) inventoryLocation);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param inventoryLocation the instance to be removed
	 * @param s the Session
	 */
	public void delete(com.floreantpos.model.inventory.InventoryLocation inventoryLocation, Session s)
	{
		delete((Object) inventoryLocation, s);
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
	public void refresh (com.floreantpos.model.inventory.InventoryLocation inventoryLocation, Session s)
	{
		refresh((Object) inventoryLocation, s);
	}


}