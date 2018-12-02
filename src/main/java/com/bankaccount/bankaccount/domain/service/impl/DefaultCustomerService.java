package com.bankaccount.bankaccount.domain.service.impl;

import java.util.UUID;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.CustomerId;
import com.bankaccount.bankaccount.domain.model.CustomerInfo;
import com.bankaccount.bankaccount.domain.model.InitialCustomerInfo;
import com.bankaccount.bankaccount.domain.service.AccountService;
import com.bankaccount.bankaccount.domain.service.CustomerService;
import com.bankaccount.bankaccount.domain.service.StoreService;

import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerService implements CustomerService
{
    private final StoreService storeService;
    private final AccountService accountService;

    public DefaultCustomerService(StoreService storeService, AccountService accountService)
    {
        this.storeService = storeService;
        this.accountService = accountService;
    }

    public CustomerId createCustomerWithOneAccount(InitialCustomerInfo customerFiles)
    {
        CustomerId previouslyExistingId = searchCustomerIdFromName(customerFiles.getFirstName(), customerFiles.getLastName());

        if (previouslyExistingId != null) throw new IllegalArgumentException("Customer already exists");

        CustomerId customerId = new CustomerId(UUID.randomUUID());

        Account account = accountService.createAccount(0d);

        CustomerInfo customerInfo = new CustomerInfo(
            customerId,
            customerFiles.getFirstName(),
            customerFiles.getLastName(),
            account.getAccountId()
        );

        storeService.createCustomer(customerInfo);

        return customerId;
    }

    public CustomerId searchCustomerIdFromName(String firstName, String lastName)
    {
        return storeService.searchCustomerByName(firstName, lastName);
    }

    public CustomerInfo searchCustomerInfoFromId(CustomerId customerId)
    {
        return storeService.searchCutomerInfoFromId(customerId);
    }
}