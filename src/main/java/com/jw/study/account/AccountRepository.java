package com.jw.study.account;

import com.jw.study.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Account findByEmail(String email);

    Account findByUsername(String username);
}
