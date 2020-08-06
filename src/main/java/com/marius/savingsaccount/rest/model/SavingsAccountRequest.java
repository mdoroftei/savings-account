package com.marius.savingsaccount.rest.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class SavingsAccountRequest {
    @NotEmpty
    String clientId;
    @NotNull
    @Min(1)
    Integer durationMonths;
    @NotNull
    @Min(0)
    Double amount;
}
