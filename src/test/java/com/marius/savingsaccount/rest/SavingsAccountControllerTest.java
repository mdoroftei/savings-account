package com.marius.savingsaccount.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marius.savingsaccount.rest.exception.OutsideWorkingDaysException;
import com.marius.savingsaccount.rest.exception.OutsideWorkingHoursException;
import com.marius.savingsaccount.rest.model.SavingsAccountRequest;
import com.marius.savingsaccount.service.SavingsAccountService;
import com.marius.savingsaccount.service.model.SavingsAccountDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SavingsAccountControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SavingsAccountService savingsAccountService;
    @MockBean
    WorkingHoursValidator workingHoursValidator;

    @Test
    public void shouldReturnMethodNotAllowedOnGet() throws Exception {
        this.mockMvc.perform(get("/savings-account")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldReturnBadRequestOnEmptyPost() throws Exception {
        this.mockMvc.perform(post("/savings-account")).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestOnMissingClientIdPost() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .durationMonths(1)
                .amount(1d)
                .build();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Validation failed")))
                .andExpect(jsonPath("errors.[0]", is("clientId: must not be empty")));
    }

    @Test
    public void shouldReturnBadRequestOnMissingDurationMonthsPost() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .amount(1d)
                .build();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Validation failed")))
                .andExpect(jsonPath("errors.[0]", is("durationMonths: must not be null")));
    }

    @Test
    public void shouldReturnBadRequestOnLessThanOneDurationMonthsPost() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .durationMonths(0)
                .amount(1d)
                .build();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Validation failed")))
                .andExpect(jsonPath("errors.[0]", is("durationMonths: must be greater than or equal to 1")));
    }

    @Test
    public void shouldReturnBadRequestOnMissingAmountPost() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .durationMonths(1)
                .build();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Validation failed")))
                .andExpect(jsonPath("errors.[0]", is("amount: must not be null")));
    }

    @Test
    public void shouldReturnBadRequestOnNegativeAmountPost() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .durationMonths(1)
                .amount(-1d)
                .build();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("Validation failed")))
                .andExpect(jsonPath("errors.[0]", is("amount: must be greater than or equal to 0")));
    }

    @Test
    public void shouldReturnBadRequestOutsideOfBusinessDays() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .durationMonths(1)
                .amount(1d)
                .build();

        doThrow(OutsideWorkingDaysException.class).when(workingHoursValidator).validate();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestOutsideOfBusinessHours() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .durationMonths(1)
                .amount(1d)
                .build();

        doThrow(OutsideWorkingHoursException.class).when(workingHoursValidator).validate();

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnSuccessPost() throws Exception {
        SavingsAccountRequest savingsAccountRequest = SavingsAccountRequest.builder()
                .clientId("clientId")
                .durationMonths(1)
                .amount(1d)
                .build();

        LocalDate localDate = LocalDate.now().plusMonths(1);

        SavingsAccountDTO savingsAccountDTO = SavingsAccountDTO.builder()
                .clientId("clientId")
                .maturityDate(localDate)
                .amount(1d)
                .build();

        when(savingsAccountService.createSavingsAccount(any(SavingsAccountDTO.class))).thenReturn(Optional.of(savingsAccountDTO));

        this.mockMvc.perform(post("/savings-account")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(savingsAccountRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("clientId", is("clientId")))
                .andExpect(jsonPath("amount", is("1.0")))
                .andExpect(jsonPath("maturityDate", is(localDate.toString())));
    }
}