package com.bankaccount.bankaccount.domain.model;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

public class AppliedOperation
{
    public AppliedOperation(IOperation operation, Double balanceBeforeApply, Double balanceAferApply, Date appliedDate)
    {
        this.operation = operation;
        this.balanceBeforeApply = balanceBeforeApply;
        this.balanceAfterApply = balanceAferApply;
        this.appliedDate = appliedDate;
    }

    @Getter
    private IOperation operation;
    @Getter
    private Double balanceBeforeApply;
    @Getter
    private Double balanceAfterApply;
    @Getter
    private Date appliedDate;

    @Override
    public String toString()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(appliedDate) + ":" 
                + getBalanceBeforeApply() + "=>" 
                + getBalanceAfterApply() + ": " 
                + getOperation();
    }
}