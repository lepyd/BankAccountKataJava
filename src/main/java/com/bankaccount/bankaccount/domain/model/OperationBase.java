package com.bankaccount.bankaccount.domain.model;

import lombok.Getter;

public class OperationBase
{
    public OperationBase(AccountId accountId, Double amount)
    {
        this.accountId = accountId;
        this.amount = amount;
    }

    @Getter
    public AccountId accountId;
    @Getter
    public Double amount;
}