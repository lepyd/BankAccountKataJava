package com.bankaccount.bankaccount.domain.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.AccountId;
import com.bankaccount.bankaccount.domain.model.AppliedOperation;
import com.bankaccount.bankaccount.domain.model.CustomerId;
import com.bankaccount.bankaccount.domain.model.CustomerInfo;
import com.bankaccount.bankaccount.domain.service.StoreService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;;

@Service
@Scope("singleton")
public class DefaultStoreService implements StoreService
{
    private Map<AccountId, Account> accounts = new HashMap<AccountId, Account>();
    private Map<CustomerId, CustomerInfo> customers = new HashMap<CustomerId, CustomerInfo>();

    public Account createAccount(Account account)
    {
        if (account == null) throw new IllegalArgumentException("account");
        if (account.getAccountId() == null) throw new IllegalArgumentException("account.AccountId");

        if(accounts.containsKey( account.getAccountId()))
        {
            throw new IllegalArgumentException(
                account.getAccountId() + " already exists");
        }
        accounts.put(account.getAccountId(), account);
        return account;
    }

    public CustomerInfo createCustomer(CustomerInfo customerInfo)
    {
        if (customerInfo == null) throw new IllegalArgumentException("customerInfo");
        if (customerInfo.getCustomerId() == null) throw new IllegalArgumentException("customerInfo.CustomerId");

        if (customers.containsKey(customerInfo.getCustomerId()))
        {
            throw new IllegalArgumentException(
                customerInfo.getCustomerId() + " already exists");
        }
        customers.put(customerInfo.getCustomerId(), customerInfo);
        return customerInfo;
    }

    public Account searchAccountFromId(AccountId accountId)
    {
        if (accountId == null)
            throw new IllegalArgumentException("accountId");
        Account existingAccount = accounts.getOrDefault(accountId, null);
        return existingAccount;
    }

    public CustomerId searchCustomerByName(String firstName, String lastName)
    {
        Optional<CustomerInfo> customerInfo = customers.values().stream()
                .filter(customer -> customer.getFirstName().equals(firstName) && customer.getLastName().equals(lastName))
                .findFirst();

        return customerInfo.isPresent() ? customerInfo.get().getCustomerId() : null;
    }

    public CustomerInfo searchCutomerInfoFromId(CustomerId customerId)
    {
        if (customerId == null) throw new IllegalArgumentException("customerId");
        CustomerInfo existingCustomer = customers.getOrDefault(customerId, null);
        return existingCustomer;
    }

    public AppliedOperation saveOperation(AppliedOperation appliedOperation)
    {
        Account account = searchAccountFromId(appliedOperation.getOperation().getAccountId());

        account.enlist(appliedOperation);

        return appliedOperation;
    }
}