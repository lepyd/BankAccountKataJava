package com.bankaccount.bankaccount.domain.service;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.AccountId;
import com.bankaccount.bankaccount.domain.model.AppliedOperation;
import com.bankaccount.bankaccount.domain.model.CustomerId;
import com.bankaccount.bankaccount.domain.model.CustomerInfo;

public interface StoreService
{
    CustomerId searchCustomerByName(String firstName, String lastName);
    CustomerInfo createCustomer(CustomerInfo customerInfo);
    Account createAccount(Account account);
    Account searchAccountFromId(AccountId accountId);
    AppliedOperation saveOperation(AppliedOperation appliedOperation);
    CustomerInfo searchCutomerInfoFromId(CustomerId customerId);
}