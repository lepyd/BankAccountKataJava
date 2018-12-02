package com.bankaccount.bankaccount.domain.service;

import com.bankaccount.bankaccount.domain.model.CustomerId;
import com.bankaccount.bankaccount.domain.model.CustomerInfo;
import com.bankaccount.bankaccount.domain.model.InitialCustomerInfo;

public interface CustomerService
{
    CustomerId searchCustomerIdFromName(String firstName, String lastName);
    CustomerId createCustomerWithOneAccount(InitialCustomerInfo customerInfo);
    CustomerInfo searchCustomerInfoFromId(CustomerId customerId);
}