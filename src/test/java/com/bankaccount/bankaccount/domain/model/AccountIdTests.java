package com.bankaccount.bankaccount.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.UUID;

import org.junit.Test;

public class AccountIdTests {
    @Test
    public void AccountId_Identical_AreEquals() {
        UUID id = UUID.randomUUID();

        AccountId accountId1 = new AccountId(id);
        AccountId accountId2 = new AccountId(id);

        assertEquals(accountId1, accountId2);
    }

    
    @Test
    public void AccountId_Different_AreNotEquals() {
        AccountId accountId1 = new AccountId( UUID.randomUUID());
        AccountId accountId2 = new AccountId( UUID.randomUUID());

        assertNotEquals(accountId1, accountId2);
    }
}