package com.marius.savingsaccount.rest.model;

import com.marius.savingsaccount.service.model.SavingsAccountDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavingsAccountResponse {
    private String clientId;
    private String amount;
    private String maturityDate;

    public static SavingsAccountResponse of(SavingsAccountDTO savingsAccountDTO) {
        return SavingsAccountResponse.builder()
                .clientId(savingsAccountDTO.getClientId())
                .amount(String.valueOf(savingsAccountDTO.getAmount()))
                .maturityDate(String.valueOf(savingsAccountDTO.getMaturityDate()))
                .build();
    }
}
