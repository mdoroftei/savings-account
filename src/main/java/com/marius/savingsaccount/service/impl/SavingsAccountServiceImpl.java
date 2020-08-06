package com.marius.savingsaccount.service.impl;

import com.marius.savingsaccount.repository.SavingsAccountRepository;
import com.marius.savingsaccount.repository.model.SavingsAccountDAO;
import com.marius.savingsaccount.rest.exception.SavingsAccountAlreadyExistsException;
import com.marius.savingsaccount.service.SavingsAccountService;
import com.marius.savingsaccount.service.model.SavingsAccountDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;

    public SavingsAccountServiceImpl(SavingsAccountRepository savingsAccountRepository) {
        this.savingsAccountRepository = savingsAccountRepository;
    }

    @Override
    public Optional<SavingsAccountDTO> createSavingsAccount(SavingsAccountDTO savingsAccountDTO) {
        if (savingsAccountDTO == null) {
            return Optional.empty();
        }

        Optional<SavingsAccountDAO> optionalExistingSavingsAccountDAO = savingsAccountRepository
                .findByClientId(savingsAccountDTO.getClientId());

        if (optionalExistingSavingsAccountDAO.isPresent()) {
            throw new SavingsAccountAlreadyExistsException("Savings Account Already exists");
        }

        SavingsAccountDAO savingsAccountDAO = SavingsAccountDAO.of(savingsAccountDTO);

        SavingsAccountDAO savedSavingsAccountDAO = savingsAccountRepository.save(savingsAccountDAO);

        return Optional.of(SavingsAccountDTO.of(savedSavingsAccountDAO));
    }
}
