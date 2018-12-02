package com.bankaccount.bankaccount.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Account
{
    private Account(AccountId accountId, Double initialBalance)
    {
        this.accountId = accountId;
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance;
        this.operationHistory = new ArrayList<AppliedOperation>();
    }

    @Getter
    private AccountId accountId;
    @Getter
    private Double initialBalance;
    @Getter
    private Double currentBalance;
    @Getter
    private List<AppliedOperation> operationHistory;

    public Account enlist(AppliedOperation appliedOperation) {
        operationHistory.add(appliedOperation);
        currentBalance = appliedOperation.getBalanceAfterApply();
        return this;
    }

    public static Account AsNewAccount(AccountId accountId, Double initialBalance)
    {
        return new Account(accountId, initialBalance);
    }
}