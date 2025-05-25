package org.openmrs.module.rmsdataexchange.api.impl;

import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.rmsdataexchange.api.BillAttributeService;
import org.openmrs.module.rmsdataexchange.api.RmsdataexchangeDao;
import org.openmrs.module.rmsdataexchange.queue.model.RMSBillAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.RMSBillAttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public class BillAttributeServiceImpl extends BaseOpenmrsService implements BillAttributeService {
	
	RmsdataexchangeDao dao;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(RmsdataexchangeDao dao) {
		this.dao = dao;
	}
	
	@Override
	public RMSBillAttribute saveBillAttribute(RMSBillAttribute billAttribute) {
		return dao.saveBillAttribute(billAttribute);
	}
	
	@Override
	public RMSBillAttribute getBillAttribute(Integer billAttributeId) {
		return dao.getBillAttribute(billAttributeId);
	}
	
	@Override
	public void deleteBillAttribute(RMSBillAttribute billAttribute) {
		dao.deleteBillAttribute(billAttribute);
	}
	
	@Override
	public List<RMSBillAttribute> getBillAttributesByBillId(Integer billId) {
		return dao.getBillAttributesByBillId(billId);
	}
	
	@Override
	public List<RMSBillAttribute> getBillAttributesByBillUuid(String billUuid) {
		return dao.getBillAttributesByBillUuid(billUuid);
	}
	
	@Override
	public List<RMSBillAttribute> getBillAttributesByTypeId(Integer billAttributeTypeId) {
		return dao.getBillAttributesByTypeId(billAttributeTypeId);
	}
	
	@Override
	public List<RMSBillAttribute> getAllBillAttributes(Boolean includeVoided) {
		return dao.getAllBillAttributes(includeVoided);
	}
	
	@Override
	public RMSBillAttributeType saveBillAttributeType(RMSBillAttributeType billAttributeType) {
		return dao.saveBillAttributeType(billAttributeType);
	}
	
	@Override
	public RMSBillAttributeType getBillAttributeType(Integer billAttributeTypeId) {
		return dao.getBillAttributeType(billAttributeTypeId);
	}
	
	@Override
	public List<RMSBillAttributeType> getAllBillAttributeTypes(Boolean includeRetired) {
		return dao.getAllBillAttributeTypes(includeRetired);
	}
	
	@Override
	public void voidBillAttribute(RMSBillAttribute billAttribute, String reason, Integer voidedBy) {
		dao.voidBillAttribute(billAttribute, reason, voidedBy);
	}
	
	@Override
	public void unvoidBillAttribute(RMSBillAttribute billAttribute) {
		dao.unvoidBillAttribute(billAttribute);
	}
	
	@Override
	public void retireBillAttributeType(RMSBillAttributeType billAttributeType, String reason, Integer retiredBy) {
		dao.retireBillAttributeType(billAttributeType, reason, retiredBy);
	}
	
	@Override
	public void unretireBillAttributeType(RMSBillAttributeType billAttributeType) {
		dao.unretireBillAttributeType(billAttributeType);
	}
}
