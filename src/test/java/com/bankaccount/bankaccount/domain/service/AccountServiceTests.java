package com.bankaccount.bankaccount.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.UUID;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.AccountId;
import com.bankaccount.bankaccount.domain.model.OperationCashDeposit;
import com.bankaccount.bankaccount.domain.model.OperationCashWithdrawal;
import com.bankaccount.bankaccount.domain.model.OperationResult;
import com.bankaccount.bankaccount.domain.model.OperationStatus;
import com.bankaccount.bankaccount.domain.service.impl.DefaultAccountService;
import com.bankaccount.bankaccount.domain.service.impl.DefaultStoreService;

import org.junit.Before;
import org.junit.Test;

public class AccountServiceTests {
    StoreService _store;

    @Before
    public void Setup() {
        _store = new DefaultStoreService(); // no need to use moq due to the current implemntation
    }

    @Test
    public void CreateAccount_WithValidAccount_Succeed() {
        AccountService accountService = new DefaultAccountService(_store);

        Account account = accountService.createAccount(100d);

        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals((Double)100d, account.getCurrentBalance());
        assertEquals((Double)100d, account.getInitialBalance());
        assertTrue(account.getOperationHistory().isEmpty());
    }

    @Test
    public void ApplyOperation_WithValidCashDeposit_Succeed() {
        AccountService accountService = new DefaultAccountService(_store);

        Account account = accountService.createAccount(100d);

        OperationResult result = accountService.applyOperation(new OperationCashDeposit(account.getAccountId(), 1000d));

        assertNotNull(result);
        assertEquals(OperationStatus.Done, result.getStatus());
        assertEquals((Double)100d, result.getResult().getBalanceBeforeApply());
        assertEquals((Double)1100d, result.getResult().getBalanceAfterApply());
        Date now = new Date();
        assertTrue( now.getTime() - result.getResult().getAppliedDate().getTime() < 100);
        assertTrue(result.getResult().getOperation() instanceof OperationCashDeposit);
        assertFalse(account.getOperationHistory().isEmpty());
    }

    @Test
    public void ApplyOperation_WithValidCashWithdrawal_Succeed() {
        AccountService accountService = new DefaultAccountService(_store);

        Account account = accountService.createAccount(1000d);

        OperationResult result = accountService
                .applyOperation(new OperationCashWithdrawal(account.getAccountId(), 100d));

        assertNotNull(result);
        assertEquals(OperationStatus.Done, result.getStatus());
        assertEquals((Double)1000d, result.getResult().getBalanceBeforeApply());
        assertEquals((Double)900d, result.getResult().getBalanceAfterApply());
        Date now = new Date();
        assertTrue( now.getTime() - result.getResult().getAppliedDate().getTime() < 100);
        assertTrue(result.getResult().getOperation() instanceof OperationCashWithdrawal);
        assertFalse(account.getOperationHistory().isEmpty());
    }

    @Test
    public void ApplyOperation_WithInvalidCashWithdrawal_IsRejected() {
        AccountService accountService = new DefaultAccountService(_store);

        Account account = accountService.createAccount(100d);

        OperationResult result = accountService
                .applyOperation(new OperationCashWithdrawal(account.getAccountId(), 1000d));

        assertNotNull(result);
        assertEquals(OperationStatus.Rejected, result.getStatus());
        assertEquals("rejected", result.getComment());
    }

    @Test
    public void ApplyOperation_WithInvalidCashDeposit_IsRejected() {
        AccountService accountService = new DefaultAccountService(_store);

        Account account = accountService.createAccount(100d);

        OperationResult result = accountService
                .applyOperation(new OperationCashDeposit(account.getAccountId(), 100000d));

        assertNotNull(result);
        assertEquals(OperationStatus.Rejected, result.getStatus());
        assertEquals("rejected", result.getComment());
    }

    @Test
    public void GetAccount_WithExistingAccount_Succeed() {
        AccountService accountService = new DefaultAccountService(_store);

        Account account = accountService.createAccount(100d);

        Account foundAccount = accountService.searchAccountFromId(account.getAccountId());

        assertEquals(account, foundAccount);
    }

    @Test
    public void GetAccount_WithInexistingAccount_ReturnsNull() {
        AccountService accountService = new DefaultAccountService(_store);

        Account foundAccount = accountService.searchAccountFromId(new AccountId(UUID.randomUUID()));

        assertNull(foundAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GetAccount_WithNullAccount_Throws()
    {
        AccountService accountService = new DefaultAccountService(_store);

        accountService.searchAccountFromId(null);
    }
}
