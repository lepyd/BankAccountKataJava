package com.bankaccount.bankaccount.domain.model;

import java.util.Date;

public class OperationCashDeposit extends OperationBase implements IOperation
{
    public OperationCashDeposit(AccountId accountId, Double amount)
    {
        super(accountId, amount);
    }

    public OperationResult applyOn(Account account)
    {
        if (!isValidFor(account)) {
            return OperationResult.AsRejected("rejected");
        }

        AppliedOperation appliedOperation = new AppliedOperation(
            this,
            account.getCurrentBalance(),
            account.getCurrentBalance() + getAmount(),
            new Date()
        );

        return OperationResult.AsDone(appliedOperation);
    }

    public Boolean isValidFor(Account account)
    {
        return getAmount() <= 10000;
    }

    @Override
    public String toString()
    {
        return "CashDeposit " + getAmount();
    }
}