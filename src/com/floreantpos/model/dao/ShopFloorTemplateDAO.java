package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.ShopFloor;
import com.floreantpos.model.ShopFloorTemplate;

public class ShopFloorTemplateDAO extends BaseShopFloorTemplateDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopFloorTemplateDAO() {
	}

	public List<ShopFloorTemplate> findByParent(ShopFloor selectedValue) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = getSession();
			criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(ShopFloorTemplate.PROP_FLOOR, selectedValue));

			return criteria.list();

		} catch (Exception e) {
		} finally {
			closeSession(session);
		}
		return null;
	}

	public void saveOrUpdateTemplates(List<ShopFloorTemplate> templates) {
		if (templates == null) {
			return;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			for (ShopFloorTemplate template : templates) {
				session.saveOrUpdate(template);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			closeSession(session);
		}

	}

	public void deleteTemplates(List<ShopFloorTemplate> templates) {
		if (templates == null) {
			return;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = createNewSession();
			tx = session.beginTransaction();
			for (ShopFloorTemplate template : templates) {
				session.delete(template);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			closeSession(session);
		}
	}
}