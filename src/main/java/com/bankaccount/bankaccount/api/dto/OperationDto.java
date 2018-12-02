package com.bankaccount.bankaccount.api.dto;

import java.util.Date;
import lombok.Data;

@Data
public class OperationDto
{
    private String status;
    private Date appliedDate;
    private Double balanceAfterApply;
    private Double amount;
    private String description;
}