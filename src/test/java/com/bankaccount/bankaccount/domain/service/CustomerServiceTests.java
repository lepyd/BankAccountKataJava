package com.bankaccount.bankaccount.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.CustomerId;
import com.bankaccount.bankaccount.domain.model.CustomerInfo;
import com.bankaccount.bankaccount.domain.model.InitialCustomerInfo;
import com.bankaccount.bankaccount.domain.service.impl.DefaultAccountService;
import com.bankaccount.bankaccount.domain.service.impl.DefaultCustomerService;
import com.bankaccount.bankaccount.domain.service.impl.DefaultStoreService;

import org.junit.Before;
import org.junit.Test;

public class CustomerServiceTests {
    StoreService storeService;
    AccountService accountService;

    @Before
    public void Setup() {
        storeService = new DefaultStoreService(); // no need to use moq due to the current implemntation
        accountService = new DefaultAccountService(storeService);
    }

    @Test
    public void CreateCustomer_WithValidInfo_Succeed()
    {
        CustomerService customerService = new DefaultCustomerService(storeService, accountService);

        InitialCustomerInfo initialCustomerInfo = new InitialCustomerInfo();
        initialCustomerInfo.setFirstName( "FirstName1" );
        initialCustomerInfo.setLastName("lastName");

        // Creating the customer
        CustomerId customerId = customerService.createCustomerWithOneAccount(initialCustomerInfo);

        assertNotNull(customerId);
        assertNotNull(customerId.getId());

        // Info is available for the customer
        CustomerInfo customerInfo = customerService.searchCustomerInfoFromId(customerId);

        assertNotNull(customerInfo);
        assertEquals(customerId, customerInfo.getCustomerId());
        assertEquals(initialCustomerInfo.getFirstName(), customerInfo.getFirstName());
        assertEquals(initialCustomerInfo.getLastName(), customerInfo.getLastName());
        assertNotNull(customerInfo.getMasterAccountId());

        // The customer has an account
        Account account = accountService.searchAccountFromId(customerInfo.getMasterAccountId());

        assertNotNull(account);
        assertEquals((Double)0d, account.getCurrentBalance());
        assertEquals((Double)0d, account.getInitialBalance());
        assertTrue(account.getOperationHistory().isEmpty());
    }
}
