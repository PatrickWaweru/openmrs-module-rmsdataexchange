package org.openmrs.module.rmsdataexchange.api.impl;

import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.rmsdataexchange.api.BillAttributeService;
import org.openmrs.module.rmsdataexchange.api.RmsdataexchangeDao;
import org.openmrs.module.rmsdataexchange.queue.model.BillAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.BillAttributeType;
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
	public BillAttribute saveBillAttribute(BillAttribute billAttribute) {
		return dao.saveBillAttribute(billAttribute);
	}
	
	@Override
	public BillAttribute getBillAttribute(Integer billAttributeId) {
		return dao.getBillAttribute(billAttributeId);
	}
	
	@Override
	public void deleteBillAttribute(BillAttribute billAttribute) {
		dao.deleteBillAttribute(billAttribute);
	}
	
	@Override
	public List<BillAttribute> getBillAttributesByBillId(Integer billId) {
		return dao.getBillAttributesByBillId(billId);
	}
	
	@Override
	public List<BillAttribute> getBillAttributesByBillUuid(String billUuid) {
		return dao.getBillAttributesByBillUuid(billUuid);
	}
	
	@Override
	public List<BillAttribute> getBillAttributesByTypeId(Integer billAttributeTypeId) {
		return dao.getBillAttributesByTypeId(billAttributeTypeId);
	}
	
	@Override
	public List<BillAttribute> getAllBillAttributes(Boolean includeVoided) {
		return dao.getAllBillAttributes(includeVoided);
	}
	
	@Override
	public BillAttributeType saveBillAttributeType(BillAttributeType billAttributeType) {
		return dao.saveBillAttributeType(billAttributeType);
	}
	
	@Override
	public BillAttributeType getBillAttributeType(Integer billAttributeTypeId) {
		return dao.getBillAttributeType(billAttributeTypeId);
	}
	
	@Override
	public List<BillAttributeType> getAllBillAttributeTypes(Boolean includeRetired) {
		return dao.getAllBillAttributeTypes(includeRetired);
	}
	
	@Override
	public void voidBillAttribute(BillAttribute billAttribute, String reason, Integer voidedBy) {
		dao.voidBillAttribute(billAttribute, reason, voidedBy);
	}
	
	@Override
	public void unvoidBillAttribute(BillAttribute billAttribute) {
		dao.unvoidBillAttribute(billAttribute);
	}
	
	@Override
	public void retireBillAttributeType(BillAttributeType billAttributeType, String reason, Integer retiredBy) {
		dao.retireBillAttributeType(billAttributeType, reason, retiredBy);
	}
	
	@Override
	public void unretireBillAttributeType(BillAttributeType billAttributeType) {
		dao.unretireBillAttributeType(billAttributeType);
	}
}
