package com.bankaccount.bankaccount.api.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.bankaccount.bankaccount.api.dto.CustomerIdDto;
import com.bankaccount.bankaccount.api.dto.OperationDto;
import com.bankaccount.bankaccount.domain.model.OperationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTests {

    @Autowired
    private MockMvc mvc;

    private <T> T callControllerWithOkResult(String path, Class<T> classe) throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(path).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        String contentAsString = result.getResponse().getContentAsString();

        T response = (T) mapper.readValue(contentAsString, classe);
        assertNotNull(response);
        return response;
    }

    @Test
    public void CreateCustomer_WithValidArgument_Succeed() throws Exception {
        CustomerIdDto reponse = callControllerWithOkResult("/CreateCustomer/pyd/yo", CustomerIdDto.class);
        assertNotNull(reponse);
    }

    @Test
    public void CreateCustomer_WithDuplicatedArgument_Conflict() throws Exception {
        callControllerWithOkResult("/CreateCustomer/pyd1/yo", CustomerIdDto.class);

        mvc.perform(MockMvcRequestBuilders.get("/CreateCustomer/pyd1/yo").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void CreateCustomer_CreateAndGetCustomer_AreSame() throws Exception {
        CustomerIdDto reponseCreate = callControllerWithOkResult("/CreateCustomer/pydd/yo", CustomerIdDto.class);
        CustomerIdDto reponseGet = callControllerWithOkResult("/GetCustomerId/pydd/yo", CustomerIdDto.class);
        assertEquals(reponseCreate, reponseGet);
    }

    @Test
    public void FullSequenceTest() throws Exception
    {
        // Creating the customer
        CustomerIdDto reponseCreate = callControllerWithOkResult("/CreateCustomer/FirstNameForFullSequence/yo", CustomerIdDto.class);

        // Apply Cash Deposit on Customer's account
        UUID customerId = reponseCreate.getId();
        OperationDto reponseDeposit = callControllerWithOkResult("/ApplyCashDeposit/"+ reponseCreate.getId() + "/1000", OperationDto.class);

        assertEquals((Double)1000d, reponseDeposit.getAmount());
        assertEquals((Double)1000d, reponseDeposit.getBalanceAfterApply());
        assertEquals("Done", reponseDeposit.getStatus());
        assertTrue(reponseDeposit.getDescription().contains("CashDeposit"));

        // Apply Cash Withdrawal on Customer's account
        OperationDto reponseWithdrawal = callControllerWithOkResult("/ApplyCashWithdrawal/"+ reponseCreate.getId() + "/200", OperationDto.class);

        assertEquals((Double)200d, reponseWithdrawal.getAmount());
        assertEquals((Double)800d, reponseWithdrawal.getBalanceAfterApply());
        assertEquals("Done", reponseWithdrawal.getStatus());
        assertTrue(reponseWithdrawal.getDescription().contains("CashWithdrawal"));

        // Get operation History
       // List<String> reponseWithdrawal = callControllerWithOkResult("/OperationHistory/"+ reponseCreate.getId(), List<String>.class);
       // assertEquals(2, reponseWithdrawal.count);
       // assertTrue(reponseWithdrawal[0].contains("CashDeposit"));
       // assertTrue(reponseWithdrawal[1].contains("CashWithdrawal"));
    }
}