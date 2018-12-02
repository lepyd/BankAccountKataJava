package com.bankaccount.bankaccount.domain.model;

import lombok.Data;

@Data
public class CustomerInfo
{
    public CustomerInfo(CustomerId customerId, String firstName, String lastName, AccountId masterAccountId) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.masterAccountId = masterAccountId;
    }

    private CustomerId customerId;
    private String firstName;
    private String lastName;
    private AccountId masterAccountId; 
}