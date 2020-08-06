package com.marius.savingsaccount.service;

import com.marius.savingsaccount.repository.SavingsAccountRepository;
import com.marius.savingsaccount.repository.model.SavingsAccountDAO;
import com.marius.savingsaccount.rest.exception.SavingsAccountAlreadyExistsException;
import com.marius.savingsaccount.service.impl.SavingsAccountServiceImpl;
import com.marius.savingsaccount.service.model.SavingsAccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavingsAccountServiceTest {

    @Mock
    private SavingsAccountRepository savingsAccountRepository;

    @InjectMocks
    private SavingsAccountServiceImpl savingsAccountService;

    @Test
    public void createSavingsAccountWithNullArgument() {
        Optional<SavingsAccountDTO> savingsAccountDTOOptional = savingsAccountService.createSavingsAccount(null);
        assertEquals(savingsAccountDTOOptional, Optional.empty());
    }

    @Test
    public void createSavingsAccountAlreadyExisting() {
        SavingsAccountDTO savingsAccountDTO = SavingsAccountDTO.builder()
                .clientId("1").build();
        SavingsAccountDAO savingsAccountDAO = SavingsAccountDAO.builder().build();

        when(savingsAccountRepository.findByClientId(any(String.class))).thenReturn(Optional.of(savingsAccountDAO));
        assertThrows(SavingsAccountAlreadyExistsException.class,
                () -> savingsAccountService.createSavingsAccount(savingsAccountDTO));
    }

    @Test
    public void createSavingsAccountSuccess() {
        String CLIENT_ID = "123123";
        SavingsAccountDTO savingsAccountDTO = SavingsAccountDTO.builder()
                .clientId(CLIENT_ID)
                .amount(1D)
                .maturityDate(LocalDate.now().plusMonths(1))
                .build();

        SavingsAccountDAO savingsAccountDAO = SavingsAccountDAO.of(savingsAccountDTO);
        when(savingsAccountRepository.findByClientId(eq(CLIENT_ID))).thenReturn(Optional.empty());
        when(savingsAccountRepository.save(any(SavingsAccountDAO.class))).thenReturn(savingsAccountDAO);

        Optional<SavingsAccountDTO> newSavingsAccountDTOOptional = savingsAccountService.createSavingsAccount(savingsAccountDTO);

        assertTrue(newSavingsAccountDTOOptional.isPresent());
        SavingsAccountDTO newSavingsAccountDTO = newSavingsAccountDTOOptional.get();
        assertEquals(savingsAccountDTO.getAmount(), newSavingsAccountDTO.getAmount());
        assertEquals(savingsAccountDTO.getClientId(), newSavingsAccountDTO.getClientId());
        assertEquals(savingsAccountDTO.getMaturityDate(), newSavingsAccountDTO.getMaturityDate());
    }
}