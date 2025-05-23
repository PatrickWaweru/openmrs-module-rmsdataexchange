/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.rmsdataexchange.api.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.DataException;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.kenyaemr.cashier.api.model.Bill;
import org.openmrs.module.kenyaemr.cashier.api.model.Payment;
import org.openmrs.module.kenyaemr.cashier.api.model.PaymentMode;
import org.openmrs.module.rmsdataexchange.api.RmsdataexchangeDao;
import org.openmrs.module.rmsdataexchange.api.util.AdviceUtils;
import org.openmrs.module.rmsdataexchange.queue.model.BillAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.BillAttributeType;
import org.openmrs.module.rmsdataexchange.queue.model.PaymentAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.PaymentAttributeType;
import org.openmrs.module.rmsdataexchange.queue.model.RmsQueue;
import org.openmrs.module.rmsdataexchange.queue.model.RmsQueueSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Criteria;

public class RmsdataexchangeDaoImpl implements RmsdataexchangeDao {
	
	private SessionFactory sessionFactory;
	
	private Boolean debugMode = AdviceUtils.isRMSLoggingEnabled();
	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Set<Payment> getPaymentsByBillId(Integer billId) {
		// Get the current Hibernate session from DbSessionFactory
		Session session = sessionFactory.getCurrentSession();
        
        // Ensure no caching is used by ignoring the cache
        session.setCacheMode(CacheMode.IGNORE);

		String sqlQuery = "SELECT distinct cbp.bill_payment_id, cbp.uuid, cbp.bill_id, cbp.payment_mode_id, cbp.amount_tendered, cbp.amount FROM cashier_bill cb inner join cashier_bill_payment cbp on cbp.bill_id = cb.bill_id and cb.bill_id =:billId";

		// Execute the query and fetch the result
        List<Object[]> resultList = session.createSQLQuery(sqlQuery)
                                           .setParameter("billId", billId)
                                           .list();
		
		if(debugMode) System.out.println("rmsdataexchange Module: Payments got SQL payments: " + resultList.size());
										   
		// Create a Set to hold the resulting Payment objects
        Set<Payment> payments = new HashSet<>();

        // Iterate through the results and map them to Payment objects
        for (Object[] row : resultList) {
            Payment payment = new Payment();
            payment.setId((Integer) row[0]);  // payment_id
            payment.setUuid((String) row[1]); // payment uuid
			Bill newBill = new Bill();
			newBill.setId(billId);
			payment.setBill(newBill); // bill
			PaymentMode newPaymentMode = new PaymentMode();
			newPaymentMode.setId((Integer) row[3]);
			payment.setInstanceType(newPaymentMode); // payment mode
			payment.setAmountTendered((BigDecimal) row[4]); //Amount Tendered
			payment.setAmount((BigDecimal) row[5]); //Total Amount
            payments.add(payment);
        }

		return(payments);
	}
	
	@Override
	public List<RmsQueue> getQueueItems() throws DataException {
		if (debugMode)
			System.out.println("rmsdataexchange Module: Getting all queued items");
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RmsQueue.class);
		criteria.add(Restrictions.eq("voided", false));
		criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
		return criteria.list();
	}
	
	@Override
	public RmsQueue saveQueueItem(RmsQueue queue) throws DAOException {
		if (debugMode)
			System.out.println("rmsdataexchange Module: Saving the RMS Queue");
		sessionFactory.getCurrentSession().saveOrUpdate(queue);
		return queue;
	}
	
	@Override
	public RmsQueue getQueueItemByUUID(String queueUUID) throws DataException {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RmsQueue.class);
		criteria.add(Restrictions.eq("uuid", queueUUID));
		return (RmsQueue) criteria.uniqueResult();
	}
	
	@Override
	public RmsQueue getQueueItemByID(Integer queueID) throws DataException {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RmsQueue.class);
		criteria.add(Restrictions.eq("id", queueID));
		return (RmsQueue) criteria.uniqueResult();
	}
	
	@Override
	public RmsQueue removeQueueItem(RmsQueue queue) throws DAOException {
		if (debugMode)
			System.out.println("rmsdataexchange Module: Removing RMS Queue Item");
		sessionFactory.getCurrentSession().delete(queue);
		return queue;
	}
	
	@Override
	public RmsQueueSystem getQueueSystemByUUID(String queueSystemUUID) throws DataException {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RmsQueueSystem.class);
		criteria.add(Restrictions.eq("uuid", queueSystemUUID));
		return (RmsQueueSystem) criteria.uniqueResult();
	}
	
	@Override
	public RmsQueueSystem getQueueSystemByID(Integer queueSystemID) throws DataException {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(RmsQueueSystem.class);
		criteria.add(Restrictions.eq("id", queueSystemID));
		return (RmsQueueSystem) criteria.uniqueResult();
	}
	
	// Payment Attributes
	
	@Override
	public PaymentAttribute savePaymentAttribute(PaymentAttribute paymentAttribute) {
		sessionFactory.getCurrentSession().saveOrUpdate(paymentAttribute);
		return paymentAttribute;
	}
	
	@Override
	public PaymentAttribute getPaymentAttribute(Integer paymentAttributeId) {
		return sessionFactory.getCurrentSession().get(PaymentAttribute.class, paymentAttributeId);
	}
	
	@Override
	public void deletePaymentAttribute(PaymentAttribute paymentAttribute) {
		sessionFactory.getCurrentSession().delete(paymentAttribute);
	}
	
	@Override
	public List<PaymentAttribute> getPaymentAttributesByPaymentId(Integer paymentId) {
		return sessionFactory.getCurrentSession()
		        .createQuery("from PaymentAttribute where billPaymentId = :paymentId", PaymentAttribute.class)
		        .setParameter("paymentId", paymentId).getResultList();
	}
	
	@Override
	public List<PaymentAttribute> getPaymentAttributesByTypeId(Integer paymentAttributeTypeId) {
		return sessionFactory.getCurrentSession()
		        .createQuery("from PaymentAttribute where paymentAttributeTypeId = :typeId", PaymentAttribute.class)
		        .setParameter("typeId", paymentAttributeTypeId).getResultList();
	}
	
	@Override
	public List<PaymentAttribute> getAllPaymentAttributes(Boolean includeVoided) {
		String query = "from PaymentAttribute";
		if (!includeVoided) {
			query += " where voided = false";
		}
		return sessionFactory.getCurrentSession().createQuery(query, PaymentAttribute.class).getResultList();
	}
	
	@Override
	public PaymentAttributeType savePaymentAttributeType(PaymentAttributeType paymentAttributeType) {
		sessionFactory.getCurrentSession().saveOrUpdate(paymentAttributeType);
		return paymentAttributeType;
	}
	
	@Override
	public PaymentAttributeType getPaymentAttributeType(Integer paymentAttributeTypeId) {
		return sessionFactory.getCurrentSession().get(PaymentAttributeType.class, paymentAttributeTypeId);
	}
	
	@Override
	public List<PaymentAttributeType> getAllPaymentAttributeTypes(Boolean includeRetired) {
		String query = "from PaymentAttributeType";
		if (!includeRetired) {
			query += " where retired = false";
		}
		return sessionFactory.getCurrentSession().createQuery(query, PaymentAttributeType.class).getResultList();
	}
	
	@Override
	public void voidPaymentAttribute(PaymentAttribute paymentAttribute, String reason, Integer voidedBy) {
		paymentAttribute.setVoided(true);
		paymentAttribute.setVoidedBy(voidedBy);
		paymentAttribute.setDateVoided(new Date());
		paymentAttribute.setVoidReason(reason);
		savePaymentAttribute(paymentAttribute);
	}
	
	@Override
	public void unvoidPaymentAttribute(PaymentAttribute paymentAttribute) {
		paymentAttribute.setVoided(false);
		paymentAttribute.setVoidedBy(null);
		paymentAttribute.setDateVoided(null);
		paymentAttribute.setVoidReason(null);
		savePaymentAttribute(paymentAttribute);
	}
	
	@Override
	public void retirePaymentAttributeType(PaymentAttributeType paymentAttributeType, String reason, Integer retiredBy) {
		paymentAttributeType.setRetired(true);
		paymentAttributeType.setRetiredBy(retiredBy);
		paymentAttributeType.setDateRetired(new Date());
		paymentAttributeType.setRetireReason(reason);
		savePaymentAttributeType(paymentAttributeType);
	}
	
	@Override
	public void unretirePaymentAttributeType(PaymentAttributeType paymentAttributeType) {
		paymentAttributeType.setRetired(false);
		paymentAttributeType.setRetiredBy(null);
		paymentAttributeType.setDateRetired(null);
		paymentAttributeType.setRetireReason(null);
		savePaymentAttributeType(paymentAttributeType);
	}
	
	@Override
	public List<PaymentAttribute> getPaymentAttributesByPaymentUuid(String paymentUuid) {
		return sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            "from PaymentAttribute pa " + "join CashierBillPayment bp on pa.billPaymentId = bp.billPaymentId "
		                    + "where bp.uuid = :paymentUuid", PaymentAttribute.class)
		        .setParameter("paymentUuid", paymentUuid).getResultList();
	}
	
	// Bill Attributes
	@Override
	public BillAttribute saveBillAttribute(BillAttribute billAttribute) {
		sessionFactory.getCurrentSession().saveOrUpdate(billAttribute);
		return billAttribute;
	}
	
	@Override
	public BillAttribute getBillAttribute(Integer billAttributeId) {
		return sessionFactory.getCurrentSession().get(BillAttribute.class, billAttributeId);
	}
	
	@Override
	public void deleteBillAttribute(BillAttribute billAttribute) {
		sessionFactory.getCurrentSession().delete(billAttribute);
	}
	
	@Override
	public List<BillAttribute> getBillAttributesByBillId(Integer billId) {
		return sessionFactory.getCurrentSession()
		        .createQuery("from CashierBillAttribute where billId = :billId", BillAttribute.class)
		        .setParameter("billId", billId).getResultList();
	}
	
	@Override
	public List<BillAttribute> getBillAttributesByBillUuid(String billUuid) {
		return sessionFactory
		        .getCurrentSession()
		        .createQuery(
		            "from CashierBillAttribute ba " + "join CashierBill b on ba.billId = b.billId "
		                    + "where b.uuid = :billUuid", BillAttribute.class).setParameter("billUuid", billUuid)
		        .getResultList();
	}
	
	@Override
	public List<BillAttribute> getBillAttributesByTypeId(Integer billAttributeTypeId) {
		return sessionFactory.getCurrentSession()
		        .createQuery("from CashierBillAttribute where paymentAttributeTypeId = :typeId", BillAttribute.class)
		        .setParameter("typeId", billAttributeTypeId).getResultList();
	}
	
	@Override
	public List<BillAttribute> getAllBillAttributes(Boolean includeVoided) {
		String query = "from CashierBillAttribute";
		if (!includeVoided) {
			query += " where voided = false";
		}
		return sessionFactory.getCurrentSession().createQuery(query, BillAttribute.class).getResultList();
	}
	
	@Override
	public BillAttributeType saveBillAttributeType(BillAttributeType billAttributeType) {
		sessionFactory.getCurrentSession().saveOrUpdate(billAttributeType);
		return billAttributeType;
	}
	
	@Override
	public BillAttributeType getBillAttributeType(Integer billAttributeTypeId) {
		return sessionFactory.getCurrentSession().get(BillAttributeType.class, billAttributeTypeId);
	}
	
	@Override
	public List<BillAttributeType> getAllBillAttributeTypes(Boolean includeRetired) {
		String query = "from CashierBillAttributeType";
		if (!includeRetired) {
			query += " where retired = false";
		}
		return sessionFactory.getCurrentSession().createQuery(query, BillAttributeType.class).getResultList();
	}
	
	@Override
	public void voidBillAttribute(BillAttribute billAttribute, String reason, Integer voidedBy) {
		billAttribute.setVoided(true);
		billAttribute.setVoidedBy(voidedBy);
		billAttribute.setDateVoided(new Date());
		billAttribute.setVoidReason(reason);
		saveBillAttribute(billAttribute);
	}
	
	@Override
	public void unvoidBillAttribute(BillAttribute billAttribute) {
		billAttribute.setVoided(false);
		billAttribute.setVoidedBy(null);
		billAttribute.setDateVoided(null);
		billAttribute.setVoidReason(null);
		saveBillAttribute(billAttribute);
	}
	
	@Override
	public void retireBillAttributeType(BillAttributeType billAttributeType, String reason, Integer retiredBy) {
		billAttributeType.setRetired(true);
		billAttributeType.setRetiredBy(retiredBy);
		billAttributeType.setDateRetired(new Date());
		billAttributeType.setRetireReason(reason);
		saveBillAttributeType(billAttributeType);
	}
	
	@Override
	public void unretireBillAttributeType(BillAttributeType billAttributeType) {
		billAttributeType.setRetired(false);
		billAttributeType.setRetiredBy(null);
		billAttributeType.setDateRetired(null);
		billAttributeType.setRetireReason(null);
		saveBillAttributeType(billAttributeType);
	}
}
