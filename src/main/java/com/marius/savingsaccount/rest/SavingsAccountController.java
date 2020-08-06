package com.marius.savingsaccount.rest;

import com.marius.savingsaccount.rest.exception.CouldNotCreateSavingsAccountException;
import com.marius.savingsaccount.rest.model.SavingsAccountRequest;
import com.marius.savingsaccount.rest.model.SavingsAccountResponse;
import com.marius.savingsaccount.service.SavingsAccountService;
import com.marius.savingsaccount.service.model.SavingsAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;
    private final WorkingHoursValidator workingHoursValidator;

    public SavingsAccountController(SavingsAccountService savingsAccountService, WorkingHoursValidator workingHoursValidator) {
        this.savingsAccountService = savingsAccountService;
        this.workingHoursValidator = workingHoursValidator;
    }

    @PostMapping("/savings-account")
    public SavingsAccountResponse createSavingsAccount(@Valid @RequestBody SavingsAccountRequest savingsAccountRequest) {
        workingHoursValidator.validate();
        log.info("creating savings account for clientId: {}", savingsAccountRequest.getClientId());

        Optional<SavingsAccountDTO> optionalSavingsAccountDTO = savingsAccountService
                .createSavingsAccount(SavingsAccountDTO.of(savingsAccountRequest));
        SavingsAccountDTO savingsAccountDTO = optionalSavingsAccountDTO.orElseThrow(() ->
                new CouldNotCreateSavingsAccountException("Could not create savings account!"));

        return SavingsAccountResponse.of(savingsAccountDTO);
    }
}

