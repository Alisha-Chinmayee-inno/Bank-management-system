package com.bank.management.system.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AccountHistoryRequest {

	private Integer employeeId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	
	
    public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public void validate() {
        if (employeeId == null) {
            throw new IllegalArgumentException("EmployeeId cannot be null.");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("StartDate cannot be null.");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("EndDate cannot be null.");
        }
    }

	 public void setStartDateFromString(String startDateStr) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        this.startDate = LocalDate.parse(startDateStr, formatter).atStartOfDay();
	    }

	    public void setEndDateFromString(String endDateStr) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        this.endDate = LocalDate.parse(endDateStr, formatter).atTime(LocalTime.MAX);
	    }
}
