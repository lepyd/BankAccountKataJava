package com.bankaccount.bankaccount.domain.service;

import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.AccountId;
import com.bankaccount.bankaccount.domain.model.IOperation;
import com.bankaccount.bankaccount.domain.model.OperationResult;

public interface AccountService
{
    Account createAccount(Double initialBalance);

    Account searchAccountFromId(AccountId accountId);

    OperationResult applyOperation(IOperation operation);
}