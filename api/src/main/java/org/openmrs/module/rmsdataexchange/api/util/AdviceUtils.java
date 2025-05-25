package org.openmrs.module.rmsdataexchange.api.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.cashier.api.model.Payment;
import org.openmrs.module.rmsdataexchange.api.RmsdataexchangeService;
import org.openmrs.module.rmsdataexchange.queue.model.RMSQueue;
import org.openmrs.module.rmsdataexchange.queue.model.RMSQueueSystem;
import java.util.concurrent.ThreadLocalRandom;

public class AdviceUtils {
	
	/**
	 * Checks if a bill/patient is in create mode or edit mode (using dateCreated) CREATE MODE =
	 * true, EDIT MODE = false
	 * 
	 * @param date
	 * @return
	 */
	public static boolean checkIfCreateModetOrEditMode(Date date) {
		// Get the current time in milliseconds
		long now = System.currentTimeMillis();
		
		// Get the time of the provided date in milliseconds
		long timeOfDate = date.getTime();
		
		// Calculate the difference in milliseconds
		long diffInMillis = now - timeOfDate;
		
		// Check if the difference is positive (date is before now) and less than 60 seconds (60,000 ms)
		return diffInMillis >= 0 && diffInMillis < 60 * 1000;
	}
	
	/**
	 * Check if there are any new payments
	 * 
	 * @param oldSet
	 * @param newSet
	 * @return
	 */
	public static Set<Payment> symmetricPaymentDifference(Set<Payment> oldSet, Set<Payment> newSet) {
        Set<Payment> result = new HashSet<>(newSet);
        Boolean debugMode = isRMSLoggingEnabled();

        // Add elements from newSet that are not in oldSet based on amount comparison
        for (Payment item1 : oldSet) {
            for (Payment item2 : newSet) {
                if(debugMode) System.out.println("rmsdataexchange Module: Payments comparison: Oldset comparing item uuid " + item2.getAmountTendered() + " with Newset: " + item1.getAmountTendered());
                // BigDecimal behaves different. You cannot use ==
                if (item1.getAmountTendered().compareTo(item2.getAmountTendered()) == 0) {
                    if(debugMode) System.out.println("rmsdataexchange Module: Payments comparison: Found a match: " + item2.getAmountTendered()+ " and: " + item1.getAmountTendered());
                    if(debugMode) System.out.println("rmsdataexchange Module: Payments comparison: Removing item amount " + item2.getAmountTendered() + " size before: " + result.size());
                    // result.remove(item2);
                    for(Payment test : result) {
                        if (item2.getAmountTendered().compareTo(test.getAmountTendered()) == 0) {
                            result.remove(test);
                            break;
                        }
                    }
                    if(debugMode) System.out.println("rmsdataexchange Module: Payments comparison: Removing item: size after: " + result.size());
                    break;
                }
            }
        }

        if(debugMode) System.out.println("rmsdataexchange Module: Payments comparison: " + result.size());

        return result;
    }
	
	/**
	 * Checks whether RMS Logging is enabled
	 * 
	 * @return true (Enabled) and false (Disabled)
	 */
	public static Boolean isRMSLoggingEnabled() {
		Boolean ret = false;
		
		GlobalProperty globalRMSEnabled = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.RMS_LOGGING_ENABLED);
		String isRMSLoggingEnabled = globalRMSEnabled.getPropertyValue();
		
		if (isRMSLoggingEnabled != null && isRMSLoggingEnabled.trim().equalsIgnoreCase("true")) {
			ret = true;
		}
		
		return (ret);
	}
	
	/**
	 * Checks whether RMS Integration is enabled
	 * 
	 * @return true (Enabled) and false (Disabled)
	 */
	public static Boolean isRMSIntegrationEnabled() {
		Boolean ret = false;
		
		GlobalProperty globalRMSEnabled = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.RMS_SYNC_ENABLED);
		String isRMSLoggingEnabled = globalRMSEnabled.getPropertyValue();
		
		if (isRMSLoggingEnabled != null && isRMSLoggingEnabled.trim().equalsIgnoreCase("true")) {
			ret = true;
		}
		
		return (ret);
	}
	
	/**
	 * Gets the RMS endpoint URL
	 * 
	 * @return
	 */
	public static String getRMSEndpointURL() {
		String ret = "";
		
		GlobalProperty globalPostUrl = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.RMS_ENDPOINT_URL);
		String baseURL = globalPostUrl.getPropertyValue();
		if (baseURL == null || baseURL.trim().isEmpty()) {
			baseURL = "https://siaya.tsconect.com/api";
		}
		ret = baseURL.trim();
		
		return (ret);
	}
	
	/**
	 * Gets the Wonder Health auth URL
	 * 
	 * @return
	 */
	public static String getWonderHealthAuthURL() {
		String ret = "";
		
		GlobalProperty globalPostUrl = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.WONDER_HEALTH_AUTH_URL);
		String baseURL = globalPostUrl.getPropertyValue();
		if (baseURL == null || baseURL.trim().isEmpty()) {
			baseURL = " https://kenyafhirtest.iwonderpro.com/FHIRAPI/create/login";
		}
		ret = baseURL.trim();
		
		return (ret);
	}
	
	/**
	 * Gets the Wonder Health auth Token
	 * 
	 * @return
	 */
	public static String getWonderHealthAuthToken() {
		String ret = "";
		
		GlobalProperty globalPostUrl = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.WONDER_HEALTH_AUTH_TOKEN);
		String token = globalPostUrl.getPropertyValue();
		if (!StringUtils.isEmpty(token)) {
			ret = token;
		}
		
		return (ret);
	}
	
	/**
	 * Gets the wonder health endpoint URL
	 * 
	 * @return
	 */
	public static String getWonderHealthEndpointURL() {
		String ret = "";
		
		GlobalProperty globalPostUrl = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.WONDERHEALTH_ENDPOINT_URL);
		String baseURL = globalPostUrl.getPropertyValue();
		if (baseURL == null || baseURL.trim().isEmpty()) {
			baseURL = "https://kenyafhirtest.iwonderpro.com/FHIRAPI/create";
		}
		ret = baseURL.trim();
		
		return (ret);
	}
	
	/**
	 * Gets the RMS Auth Username
	 * 
	 * @return
	 */
	public static String getRMSAuthUserName() {
		String ret = "";
		
		GlobalProperty rmsUserGP = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.RMS_USERNAME);
		String rmsUser = rmsUserGP.getPropertyValue();
		ret = (rmsUser == null || rmsUser.trim().isEmpty()) ? "" : rmsUser.trim();
		
		return (ret);
	}
	
	/**
	 * Gets the RMS Auth Password
	 * 
	 * @return
	 */
	public static String getRMSAuthPassword() {
		String ret = "";
		
		GlobalProperty rmsPasswordGP = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.RMS_PASSWORD);
		String rmsPassword = rmsPasswordGP.getPropertyValue();
		ret = (rmsPassword == null || rmsPassword.trim().isEmpty()) ? "" : rmsPassword.trim();
		
		return (ret);
	}
	
	/**
	 * Checks whether Wonder Health Integration is enabled
	 * 
	 * @return true (Enabled) and false (Disabled)
	 */
	public static Boolean isWonderHealthIntegrationEnabled() {
		Boolean ret = false;
		
		GlobalProperty globalWONDERHEALTHEnabled = Context.getAdministrationService().getGlobalPropertyObject(
		    RMSModuleConstants.WONDERHEALTH_SYNC_ENABLED);
		String isWONDERHEALTHLoggingEnabled = globalWONDERHEALTHEnabled.getPropertyValue();
		
		if (isWONDERHEALTHLoggingEnabled != null && isWONDERHEALTHLoggingEnabled.trim().equalsIgnoreCase("true")) {
			ret = true;
		}
		
		return (ret);
	}
	
	/**
	 * Gets a random integer between lower and upper
	 * 
	 * @param lower
	 * @param upper
	 * @return
	 */
	public static int getRandomInt(int lower, int upper) {
		if (lower > upper) {
			throw new IllegalArgumentException(
			        "rmsdataexchange Module: getRandomInt Error : Lower limit must be less than or equal to upper limit");
		}
		return ThreadLocalRandom.current().nextInt(lower, upper + 1);
	}
	
	/**
	 * Adds payload to queue for later processing
	 * 
	 * @param payload
	 * @return
	 */
	public static Boolean addSyncPayloadToQueue(String payload, RMSQueueSystem rmsQueueSystem) {
		Boolean ret = false;
		Boolean debugMode = isRMSLoggingEnabled();
		try {
			// get the system
			RmsdataexchangeService rmsdataexchangeService = Context.getService(RmsdataexchangeService.class);
			if (rmsdataexchangeService != null) {
				if (rmsQueueSystem != null) {
					RMSQueue rmsQueue = new RMSQueue();
					rmsQueue.setPayload(payload);
					rmsQueue.setSystem(rmsQueueSystem);
					
					rmsdataexchangeService.saveQueueItem(rmsQueue);
					return (true);
				} else {
					if (debugMode)
						System.err
						        .println("rmsdataexchange Module: Error saving payload to the queue: Failed to get the queue system");
				}
			} else {
				if (debugMode)
					System.err
					        .println("rmsdataexchange Module: Error saving payload to the queue: Failed to load RMS service");
			}
			
		}
		catch (Exception ex) {
			if (debugMode)
				System.err.println("rmsdataexchange Module: Error saving payload to the queue: " + ex.getMessage());
			ex.printStackTrace();
		}
		return (ret);
	}
	
	/**
	 * Get the status of sync chores
	 * 
	 * @return true - already synced, false - not synced
	 */
	// public static Boolean getRMSSyncStatus() {
	// 	Boolean ret = false;
	
	// 	GlobalProperty rmsPatientSyncStatusGP = Context.getAdministrationService().getGlobalPropertyObject(
	// 	    RMSModuleConstants.RMS_PATIENT_SYNC_STATUS);
	// 	GlobalProperty rmsBillSyncStatusGP = Context.getAdministrationService().getGlobalPropertyObject(
	// 	    RMSModuleConstants.RMS_BILL_SYNC_STATUS);
	// 	String rmsPatientSyncStatus = rmsPatientSyncStatusGP.getPropertyValue();
	// 	String rmsBillSyncStatus = rmsBillSyncStatusGP.getPropertyValue();
	// 	String patientTest = (rmsPatientSyncStatus == null || rmsPatientSyncStatus.trim().isEmpty()) ? ""
	// 	        : rmsPatientSyncStatus.trim();
	// 	String billTest = (rmsBillSyncStatus == null || rmsBillSyncStatus.trim().isEmpty()) ? "" : rmsBillSyncStatus.trim();
	
	// 	if (patientTest.equalsIgnoreCase("true") && billTest.equalsIgnoreCase("true")) {
	// 		return (true);
	// 	}
	
	// 	return (ret);
	// }
	
	/**
	 * Mark the sync chores as done
	 * 
	 * @return
	 */
	// public static void setRMSSyncStatus(Boolean status) {
	
	// 	GlobalProperty rmsPatientSyncStatusGP = Context.getAdministrationService().getGlobalPropertyObject(
	// 	    RMSModuleConstants.RMS_PATIENT_SYNC_STATUS);
	// 	GlobalProperty rmsBillSyncStatusGP = Context.getAdministrationService().getGlobalPropertyObject(
	// 	    RMSModuleConstants.RMS_BILL_SYNC_STATUS);
	// 	if (status) {
	// 		rmsPatientSyncStatusGP.setPropertyValue("true");
	// 		rmsBillSyncStatusGP.setPropertyValue("true");
	// 	} else {
	// 		rmsPatientSyncStatusGP.setPropertyValue("false");
	// 		rmsBillSyncStatusGP.setPropertyValue("false");
	// 	}
	// }
	
}
