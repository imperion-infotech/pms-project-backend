package com.pms.auditlog.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log")

public class AuditLog {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String action;
	    private String entityName;
	    private String entityId;

	    @Column(columnDefinition = "TEXT")
	    private String oldValue;

	    @Column(columnDefinition = "TEXT")
	    private String newValue;

	    private String username;
	    private String traceId;
	    private String methodName;

	    private LocalDateTime timestamp;
	    
	    private boolean success;
	    private String errorMessage;
	    private Long hotelId;
	    @Column(name = "request_trace_id")
	    private String requestTraceId;

	    @Column(name = "business_trace_id")
	    private String businessTraceId;
	    
		public String getRequestTraceId() {
			return requestTraceId;
		}
		public void setRequestTraceId(String requestTraceId) {
			this.requestTraceId = requestTraceId;
		}
		public String getBusinessTraceId() {
			return businessTraceId;
		}
		public void setBusinessTraceId(String businessTraceId) {
			this.businessTraceId = businessTraceId;
		}
		public Long getHotelId() {
			return hotelId;
		}
		public void setHotelId(Long hotelId) {
			this.hotelId = hotelId;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getEntityName() {
			return entityName;
		}
		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}
		public String getEntityId() {
			return entityId;
		}
		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}
		public String getOldValue() {
			return oldValue;
		}
		public void setOldValue(String oldValue) {
			this.oldValue = oldValue;
		}
		public String getNewValue() {
			return newValue;
		}
		public void setNewValue(String newValue) {
			this.newValue = newValue;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getTraceId() {
			return traceId;
		}
		public void setTraceId(String traceId) {
			this.traceId = traceId;
		}
		public String getMethodName() {
			return methodName;
		}
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

    // getters & setters
	    
	    
    
    
}