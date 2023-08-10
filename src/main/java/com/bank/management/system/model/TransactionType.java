package com.bank.management.system.model;

public enum TransactionType {
    DEPOSIT(1, "Deposit"),
    WITHDRAWAL(2, "Withdrawal"),
    ACCOUNT_CREATED(3, "Account_Created"),
    CHECKED_HISTORY(4, "Checked_History");

    private final Integer code;
    private final String value;

    TransactionType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
    public boolean isDeposit(Integer code) {
        return code != null && this.code.equals(code);
    }

    public boolean isDeposit(String value) {
        return value != null && this.value.equals(value);
    }
    
}
