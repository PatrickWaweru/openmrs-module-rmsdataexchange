package org.openmrs.module.rmsdataexchange.api.impl;

import org.openmrs.module.rmsdataexchange.api.PaymentAttributeService;
import org.openmrs.module.rmsdataexchange.api.RmsdataexchangeDao;
import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.rmsdataexchange.queue.model.PaymentAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.PaymentAttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.openmrs.api.impl.BaseOpenmrsService;

import java.util.Date;
import java.util.List;

public class PaymentAttributeServiceImpl extends BaseOpenmrsService implements PaymentAttributeService {
	
	RmsdataexchangeDao dao;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(RmsdataexchangeDao dao) {
		this.dao = dao;
	}
	
	@Override
	public PaymentAttribute savePaymentAttribute(PaymentAttribute paymentAttribute) {
		return dao.savePaymentAttribute(paymentAttribute);
	}
	
	@Override
	public PaymentAttribute getPaymentAttribute(Integer paymentAttributeId) {
		return dao.getPaymentAttribute(paymentAttributeId);
	}
	
	@Override
	public void deletePaymentAttribute(PaymentAttribute paymentAttribute) {
		dao.deletePaymentAttribute(paymentAttribute);
	}
	
	@Override
	public List<PaymentAttribute> getPaymentAttributesByPaymentId(Integer paymentId) {
		return dao.getPaymentAttributesByPaymentId(paymentId);
	}
	
	@Override
	public List<PaymentAttribute> getPaymentAttributesByTypeId(Integer paymentAttributeTypeId) {
		return dao.getPaymentAttributesByTypeId(paymentAttributeTypeId);
	}
	
	@Override
	public List<PaymentAttribute> getAllPaymentAttributes(Boolean includeVoided) {
		return dao.getAllPaymentAttributes(includeVoided);
	}
	
	@Override
	public PaymentAttributeType savePaymentAttributeType(PaymentAttributeType paymentAttributeType) {
		return dao.savePaymentAttributeType(paymentAttributeType);
	}
	
	@Override
	public PaymentAttributeType getPaymentAttributeType(Integer paymentAttributeTypeId) {
		return dao.getPaymentAttributeType(paymentAttributeTypeId);
	}
	
	@Override
	public List<PaymentAttributeType> getAllPaymentAttributeTypes(Boolean includeRetired) {
		return dao.getAllPaymentAttributeTypes(includeRetired);
	}
	
	@Override
	public void voidPaymentAttribute(PaymentAttribute paymentAttribute, String reason, Integer voidedBy) {
		dao.voidPaymentAttribute(paymentAttribute, reason, voidedBy);
	}
	
	@Override
	public void unvoidPaymentAttribute(PaymentAttribute paymentAttribute) {
		dao.unvoidPaymentAttribute(paymentAttribute);
	}
	
	@Override
	public void retirePaymentAttributeType(PaymentAttributeType paymentAttributeType, String reason, Integer retiredBy) {
		dao.retirePaymentAttributeType(paymentAttributeType, reason, retiredBy);
	}
	
	@Override
	public void unretirePaymentAttributeType(PaymentAttributeType paymentAttributeType) {
		dao.unretirePaymentAttributeType(paymentAttributeType);
	}
	
	@Override
	public List<PaymentAttribute> getPaymentAttributesByPaymentUuid(String paymentUuid) {
		return dao.getPaymentAttributesByPaymentUuid(paymentUuid);
	}
}
