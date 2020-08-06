package com.marius.savingsaccount.service;

import com.marius.savingsaccount.service.model.SavingsAccountDTO;

import java.util.Optional;

public interface SavingsAccountService {
    Optional<SavingsAccountDTO> createSavingsAccount(SavingsAccountDTO savingsAccountDTO);
}
