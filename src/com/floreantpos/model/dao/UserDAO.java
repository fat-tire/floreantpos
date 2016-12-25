/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model.dao;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.EmployeeInOutHistory;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.util.UserNotFoundException;

public class UserDAO extends BaseUserDAO {
	public final static UserDAO instance = new UserDAO();

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public UserDAO() {
	}

	public List<User> findAllActive() {
		Session session = null;

		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			Junction activeUserCriteria = Restrictions.disjunction().add(Restrictions.isNull(User.PROP_ACTIVE))
					.add(Restrictions.eq(User.PROP_ACTIVE, Boolean.TRUE));
			criteria.add(activeUserCriteria);
			criteria.add(Restrictions.eq(User.PROP_CLOCKED_IN, Boolean.TRUE));

			return criteria.list();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<User> findDrivers() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(User.PROP_DRIVER, Boolean.TRUE));

			return criteria.list();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public User findUser(int id) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(User.PROP_USER_ID, id));

			Object result = criteria.uniqueResult();
			if (result != null) {
				return (User) result;
			}
			else {
				//TODO: externalize string
				throw new UserNotFoundException(Messages.getString("UserDAO.0") + id + Messages.getString("UserDAO.1")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public User findUserBySecretKey(String secretKey) {
		Session session = null;

		try {

			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(User.PROP_PASSWORD, secretKey));

			Object result = criteria.uniqueResult();
			return (User) result;
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public boolean isUserExist(int id) {
		try {
			User user = findUser(id);

			return user != null;

		} catch (UserNotFoundException x) {
			return false;
		}
	}

	public Integer findUserWithMaxId() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.setProjection(Projections.max(User.PROP_USER_ID));

			List list = criteria.list();
			if (list != null && list.size() > 0) {
				return (Integer) list.get(0);
			}

			return null;
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public List<User> getClockedInUser(Terminal terminal) {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(User.PROP_CLOCKED_IN, Boolean.TRUE));
			criteria.add(Restrictions.eq(User.PROP_CURRENT_TERMINAL, terminal));

			return criteria.list();
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}

	}

	public void saveClockIn(User user, AttendenceHistory attendenceHistory, Shift shift, Calendar currentTime) {
		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(user);
			session.saveOrUpdate(attendenceHistory);

			tx.commit();
		} catch (Exception e) {
			PosLog.error(getClass(), e);

			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception x) {
				}
			}
			throw new PosException(Messages.getString("UserDAO.2"), e); //$NON-NLS-1$

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public void saveClockOut(User user, AttendenceHistory attendenceHistory, Shift shift, Calendar currentTime) {
		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(user);
			session.saveOrUpdate(attendenceHistory);

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception x) {
				}
			}
			throw new PosException(Messages.getString("UserDAO.3"), e); //$NON-NLS-1$

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public void saveDriverOut(User user, EmployeeInOutHistory attendenceHistory, Shift shift, Calendar currentTime) {
		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(user);
			session.saveOrUpdate(attendenceHistory);

			tx.commit();
		} catch (Exception e) {
			PosLog.error(getClass(), e);

			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception x) {
				}
			}
			throw new PosException(Messages.getString("UserDAO.2"), e); //$NON-NLS-1$

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	public void saveDriverIn(User user, EmployeeInOutHistory attendenceHistory, Shift shift, Calendar currentTime) {
		Session session = null;
		Transaction tx = null;

		try {
			session = getSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(user);
			session.saveOrUpdate(attendenceHistory);

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception x) {
				}
			}
			throw new PosException(Messages.getString("UserDAO.3"), e); //$NON-NLS-1$

		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
	}

	private boolean validate(User user, boolean editMode) throws PosException {
		String hql = "from User u where u.userId=:userId and u.type=:userType"; //$NON-NLS-1$

		Session session = getSession();
		Query query = session.createQuery(hql);
		query = query.setParameter("userId", user.getUserId()); //$NON-NLS-1$
		query = query.setParameter("userType", user.getType()); //$NON-NLS-1$

		if (query.list().size() > 0) {
			throw new PosException(Messages.getString("UserDAO.7")); //$NON-NLS-1$
		}

		return true;
	}

	public void saveOrUpdate(User user, boolean editMode) {
		Session session = null;

		try {
			if (!editMode) {
				validate(user, editMode);
			}
			super.saveOrUpdate(user);
		} catch (Exception x) {
			throw new PosException(Messages.getString("UserDAO.8"), x); //$NON-NLS-1$
		} finally {
			closeSession(session);
		}

	}

	// public User findByPassword(String password) throws PosException {
	// Session session = null;
	// Transaction tx = null;
	//		
	// String hql = "from User u where u.password=:password";
	//		
	// try {
	// session = getSession();
	// tx = session.beginTransaction();
	// Query query = session.createQuery(hql);
	// query = query.setParameter("password", password);
	// User user = (User) query.uniqueResult();
	// tx.commit();
	// if(user == null) {
	// throw new PosException("User not found");
	// }
	// return user;
	// } catch(PosException x) {
	// throw x;
	// } catch (Exception e) {
	// try {
	// if(tx != null) {
	// tx.rollback();
	// }
	// }catch(Exception e2) {}
	// throw new PosException("Unnable to find user", e);
	// } finally {
	// if(session != null) {
	// session.close();
	// }
	// }
	// }

	public int findNumberOfOpenTickets(User user) throws PosException {
		Session session = null;
		Transaction tx = null;

		String hql = "select count(*) from Ticket ticket where ticket.owner=:owner and ticket." //$NON-NLS-1$
				+ Ticket.PROP_CLOSED + "settled=false"; //$NON-NLS-1$
		int count = 0;
		try {
			session = getSession();
			tx = session.beginTransaction();
			Query query = session.createQuery(hql);
			query = query.setEntity("owner", user); //$NON-NLS-1$
			Iterator iterator = query.iterate();
			if (iterator.hasNext()) {
				count = ((Integer) iterator.next()).intValue();
			}
			tx.commit();
			return count;
		} catch (Exception e) {
			try {
				if (tx != null) {
					tx.rollback();
				}
			} catch (Exception e2) {
			}
			throw new PosException(Messages.getString("UserDAO.12"), e); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}