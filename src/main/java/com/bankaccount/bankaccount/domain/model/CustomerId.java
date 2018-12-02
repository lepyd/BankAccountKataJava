package com.bankaccount.bankaccount.domain.model;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class CustomerId
{
    public CustomerId(UUID id)
    {
        this.id = id;
    }

    @Getter
    UUID id;
}