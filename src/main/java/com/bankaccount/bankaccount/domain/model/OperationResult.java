package com.bankaccount.bankaccount.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class OperationResult
{
    @Getter
    public OperationStatus status;
    @Getter
    public String comment;
    @Getter
    public AppliedOperation result;

    public static OperationResult AsRejected(String comment)
    {
        OperationResult result = new OperationResult();
        result.status = OperationStatus.Rejected;
        result.comment = comment;
        return result;
    }

    public static OperationResult AsFailed(String comment)
    {
        OperationResult result = new OperationResult();
        result.status = OperationStatus.Failed;
        result.comment = comment;
        return result;
    }

    public static OperationResult AsDone(AppliedOperation appliedOperation)
    {
        return AsDone(appliedOperation, null);
    }
    public static OperationResult AsDone(AppliedOperation appliedOperation, String comment)
    {
        OperationResult result = new OperationResult();
        result.status = OperationStatus.Done;
        result.result = appliedOperation;
        result.comment = comment;
        return result;
    }
}