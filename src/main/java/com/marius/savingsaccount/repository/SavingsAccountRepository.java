package com.marius.savingsaccount.repository;

import com.marius.savingsaccount.repository.model.SavingsAccountDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccountDAO, String> {
    Optional<SavingsAccountDAO> findByClientId(String clientId);
}
