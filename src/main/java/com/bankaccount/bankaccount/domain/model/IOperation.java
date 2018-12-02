package com.bankaccount.bankaccount.domain.model;

public interface IOperation
{
    AccountId getAccountId();
    Double getAmount();

    Boolean isValidFor(Account account);
    OperationResult applyOn(Account account);
}