package com.marius.savingsaccount.service.model;

import com.marius.savingsaccount.repository.model.SavingsAccountDAO;
import com.marius.savingsaccount.rest.model.SavingsAccountRequest;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SavingsAccountDTO {

    private Long id;
    private String clientId;
    private LocalDate maturityDate;
    private Double amount;

    public static SavingsAccountDTO of(SavingsAccountRequest savingsAccountRequest) {
        return SavingsAccountDTO.builder()
                .clientId(savingsAccountRequest.getClientId())
                .maturityDate(LocalDate.now().plusMonths(savingsAccountRequest.getDurationMonths()))
                .amount(savingsAccountRequest.getAmount())
                .build();
    }

    public static SavingsAccountDTO of(SavingsAccountDAO savingsAccountDAO) {
        return SavingsAccountDTO.builder()
                .id(savingsAccountDAO.getId())
                .clientId(savingsAccountDAO.getClientId())
                .maturityDate(savingsAccountDAO.getMaturityDate())
                .amount(savingsAccountDAO.getAmount())
                .build();
    }
}
