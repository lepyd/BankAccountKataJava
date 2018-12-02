package com.bankaccount.bankaccount.api.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bankaccount.bankaccount.api.dto.CustomerIdDto;
import com.bankaccount.bankaccount.api.dto.OperationDto;
import com.bankaccount.bankaccount.domain.model.Account;
import com.bankaccount.bankaccount.domain.model.CustomerId;
import com.bankaccount.bankaccount.domain.model.CustomerInfo;
import com.bankaccount.bankaccount.domain.model.IOperation;
import com.bankaccount.bankaccount.domain.model.InitialCustomerInfo;
import com.bankaccount.bankaccount.domain.model.OperationCashDeposit;
import com.bankaccount.bankaccount.domain.model.OperationCashWithdrawal;
import com.bankaccount.bankaccount.domain.model.OperationResult;
import com.bankaccount.bankaccount.domain.model.OperationStatus;
import com.bankaccount.bankaccount.domain.service.AccountService;
import com.bankaccount.bankaccount.domain.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CustomerController {
    
    CustomerService _customerService;
    AccountService _accountService;

    public CustomerController(
        CustomerService customerService,
        AccountService accountService)
    {
        _customerService = customerService;
        _accountService = accountService;
    }

    @GetMapping("CreateCustomer/{firstName}/{lastName}")
    public ResponseEntity<CustomerIdDto> CreateCustomer(@PathVariable String firstName, @PathVariable String lastName)
    {
        InitialCustomerInfo initialCustomerInfo = new InitialCustomerInfo();
            initialCustomerInfo.setFirstName( firstName);
            initialCustomerInfo.setLastName( lastName);
            
        try
        {
            CustomerId customerId = _customerService.createCustomerWithOneAccount(initialCustomerInfo);

            CustomerIdDto dto = new CustomerIdDto(); dto.setId( customerId.getId() );

            return new ResponseEntity<>(
                dto, 
                HttpStatus.OK);

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(
                null, 
                HttpStatus.CONFLICT);
        }
    }

    @GetMapping("GetCustomerId/{firstName}/{lastName}")
    public ResponseEntity<CustomerIdDto> GetCustomerId(@PathVariable String firstName, @PathVariable String lastName)
    {
        CustomerId customerId = _customerService.searchCustomerIdFromName(firstName, lastName);

        if(customerId == null)
        {
            return new ResponseEntity<>(
                null, 
                HttpStatus.NO_CONTENT);
        }

        CustomerIdDto dto = new CustomerIdDto();
        dto.setId( customerId.getId());

        return new ResponseEntity<>(
            dto, 
            HttpStatus.OK);
    }

    @GetMapping("ApplyCashWithdrawal/{customerId}/{amount}")
    public ResponseEntity<OperationDto> ApplyCashWithdrawal(@PathVariable UUID customerId, @PathVariable Double amount)
    {
        CustomerId cid = new CustomerId(customerId);

        CustomerInfo customerInfo = _customerService.searchCustomerInfoFromId(cid);

        IOperation operation = new OperationCashWithdrawal(customerInfo.getMasterAccountId(), amount);

        OperationResult operationResult = _accountService.applyOperation(operation);

        OperationDto dto = Map(operationResult);

        return new ResponseEntity<>(
            dto, 
            HttpStatus.OK);
    }

    private OperationDto Map(OperationResult operationResult)
    {
        OperationDto dto = new OperationDto();
        if (operationResult.getStatus() == OperationStatus.Done)
        {
            dto.setStatus( operationResult.getStatus().toString() );
            dto.setAmount( operationResult.getResult().getOperation().getAmount());
            dto.setBalanceAfterApply( operationResult.getResult().getBalanceAfterApply());
            dto.setAppliedDate( operationResult.getResult().getAppliedDate());
            dto.setDescription(operationResult.getResult().toString());
        }
        else
        {
            dto.setStatus( operationResult.getStatus().toString());
            dto.setDescription( operationResult.getComment());
        }
        return dto;
    }

    @GetMapping("ApplyCashDeposit/{customerId}/{amount}")
    public ResponseEntity<OperationDto> ApplyCashDeposit(@PathVariable UUID customerId, @PathVariable Double amount)
    {
        CustomerId cid = new CustomerId(customerId);

        CustomerInfo customerInfo = _customerService.searchCustomerInfoFromId(cid);

        IOperation operation = new OperationCashDeposit(customerInfo.getMasterAccountId(), amount);

        OperationResult operationResult = _accountService.applyOperation(operation);

        OperationDto dto = Map(operationResult);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("OperationHistory/{customerId}")
    public ResponseEntity<List<String>> OperationHistory(@PathVariable UUID customerId)
    {
        CustomerId cid = new CustomerId(customerId);

        CustomerInfo customerInfo = _customerService.searchCustomerInfoFromId(cid);

        if (customerInfo == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        Account account = _accountService.searchAccountFromId(customerInfo.getMasterAccountId());

        List<String> history = account.getOperationHistory().stream().map(op -> op.toString())
                .collect(Collectors.toList());

        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
