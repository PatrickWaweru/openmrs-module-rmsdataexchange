package org.openmrs.module.rmsdataexchange.api;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.rmsdataexchange.queue.model.RMSPaymentAttribute;
import org.openmrs.module.rmsdataexchange.queue.model.RMSPaymentAttributeType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface RMSPaymentAttributeService extends OpenmrsService {
	
	// CRUD Operations
	RMSPaymentAttribute savePaymentAttribute(RMSPaymentAttribute paymentAttribute);
	
	RMSPaymentAttribute getPaymentAttribute(Integer paymentAttributeId);
	
	void deletePaymentAttribute(RMSPaymentAttribute paymentAttribute);
	
	List<RMSPaymentAttribute> getPaymentAttributesByPaymentUuid(String paymentUuid);
	
	// Query Operations
	List<RMSPaymentAttribute> getPaymentAttributesByPaymentId(Integer paymentId);
	
	List<RMSPaymentAttribute> getPaymentAttributesByTypeId(Integer paymentAttributeTypeId);
	
	List<RMSPaymentAttribute> getAllPaymentAttributes(Boolean includeVoided);
	
	// Type Operations
	RMSPaymentAttributeType savePaymentAttributeType(RMSPaymentAttributeType paymentAttributeType);
	
	RMSPaymentAttributeType getPaymentAttributeType(Integer paymentAttributeTypeId);
	
	List<RMSPaymentAttributeType> getAllPaymentAttributeTypes(Boolean includeRetired);
	
	// Utility Operations
	void voidPaymentAttribute(RMSPaymentAttribute paymentAttribute, String reason, Integer voidedBy);
	
	void unvoidPaymentAttribute(RMSPaymentAttribute paymentAttribute);
	
	void retirePaymentAttributeType(RMSPaymentAttributeType paymentAttributeType, String reason, Integer retiredBy);
	
	void unretirePaymentAttributeType(RMSPaymentAttributeType paymentAttributeType);
	
}
