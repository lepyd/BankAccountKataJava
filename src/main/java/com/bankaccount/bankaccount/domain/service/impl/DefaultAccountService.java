package com.bankaccount.bankaccount.domain.service.impl;

import java.util.UUID;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.AccountId;
import com.bankaccount.bankaccount.domain.model.IOperation;
import com.bankaccount.bankaccount.domain.model.OperationResult;
import com.bankaccount.bankaccount.domain.model.OperationStatus;
import com.bankaccount.bankaccount.domain.service.AccountService;
import com.bankaccount.bankaccount.domain.service.StoreService;

import org.springframework.stereotype.Service;

@Service
public class DefaultAccountService implements AccountService
{
    StoreService storeService;

    public DefaultAccountService(StoreService storeService)
    {
        this.storeService = storeService;
    }

    public OperationResult applyOperation(IOperation operation)
    {
        try
        {
            Account account = searchAccountFromId(operation.getAccountId());
            OperationResult operationResult = operation.applyOn(account);

            if (operationResult.getStatus() == OperationStatus.Done)
            {
                storeService.saveOperation(operationResult.getResult());
            }

            return operationResult;
        }
        catch (Exception e)
        {
            return OperationResult.AsFailed(e.getMessage());
        }
    }

    public Account createAccount(Double initialBalance)
    {
        Account account = Account.AsNewAccount(
            new AccountId(UUID.randomUUID()), 
            initialBalance);

        Account savedAccount = storeService.createAccount(account);

        return savedAccount;
    }

    public Account searchAccountFromId(AccountId accountId)
    {
        return storeService.searchAccountFromId(accountId);
    }
}