package com.marius.savingsaccount.repository.model;

import com.marius.savingsaccount.service.model.SavingsAccountDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class SavingsAccountDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String clientId;
    private LocalDate maturityDate;
    private Double amount;

    SavingsAccountDAO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SavingsAccountDAO(Long id, String clientId, LocalDate maturityDate, Double amount) {
        this.id = id;
        this.clientId = clientId;
        this.maturityDate = maturityDate;
        this.amount = amount;
    }

    public static SavingsAccountDAO of(SavingsAccountDTO savingsAccountDTO) {
        return SavingsAccountDAO.builder()
                .clientId(savingsAccountDTO.getClientId())
                .maturityDate(savingsAccountDTO.getMaturityDate())
                .amount(savingsAccountDTO.getAmount())
                .build();
    }

    public static SavingsAccountDAOBuilder builder() {
        return new SavingsAccountDAOBuilder();
    }

    public static class SavingsAccountDAOBuilder {
        private Long id;
        private String clientId;
        private LocalDate maturityDate;
        private Double amount;

        public SavingsAccountDAOBuilder() {
        }

        public SavingsAccountDAOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SavingsAccountDAOBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public SavingsAccountDAOBuilder maturityDate(LocalDate maturityDate) {
            this.maturityDate = maturityDate;
            return this;
        }

        public SavingsAccountDAOBuilder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public SavingsAccountDAO build() {
            return new SavingsAccountDAO(id, clientId, maturityDate, amount);
        }
    }
}
