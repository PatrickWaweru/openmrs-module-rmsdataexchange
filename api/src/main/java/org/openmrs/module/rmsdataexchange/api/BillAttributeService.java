package org.openmrs.module.rmsdataexchange.api;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.rmsdataexchange.queue.model.RMSBillAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.RMSBillAttributeType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BillAttributeService extends OpenmrsService {
	
	// CRUD Operations
	RMSBillAttribute saveBillAttribute(RMSBillAttribute billAttribute);
	
	RMSBillAttribute getBillAttribute(Integer billAttributeId);
	
	void deleteBillAttribute(RMSBillAttribute billAttribute);
	
	// Query Operations
	List<RMSBillAttribute> getBillAttributesByBillId(Integer billId);
	
	List<RMSBillAttribute> getBillAttributesByBillUuid(String billUuid);
	
	List<RMSBillAttribute> getBillAttributesByTypeId(Integer billAttributeTypeId);
	
	List<RMSBillAttribute> getAllBillAttributes(Boolean includeVoided);
	
	// Type Operations
	RMSBillAttributeType saveBillAttributeType(RMSBillAttributeType billAttributeType);
	
	RMSBillAttributeType getBillAttributeType(Integer billAttributeTypeId);
	
	List<RMSBillAttributeType> getAllBillAttributeTypes(Boolean includeRetired);
	
	// Utility Operations
	void voidBillAttribute(RMSBillAttribute billAttribute, String reason, Integer voidedBy);
	
	void unvoidBillAttribute(RMSBillAttribute billAttribute);
	
	void retireBillAttributeType(RMSBillAttributeType billAttributeType, String reason, Integer retiredBy);
	
	void unretireBillAttributeType(RMSBillAttributeType billAttributeType);
}
