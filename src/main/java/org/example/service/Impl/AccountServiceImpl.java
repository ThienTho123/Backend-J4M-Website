package org.example.service.Impl;

import jakarta.validation.Valid;
import org.example.auth.RegisterRequest;
import org.example.auth.RegisterRequestForAdmin;
import org.example.entity.*;
import org.example.entity.enums.Role;

import lombok.RequiredArgsConstructor;
import org.example.entity.enums.Status;
import org.example.repository.AccountRepository;
import org.example.service.IAccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Account getAccountById(Integer accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        return optionalAccount.orElse(null);
    }




    @Override
    public Account makeAccount(RegisterRequest request) {
        return Account.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.user)
                .status(Status.Enable)
                .build();
    }

    @Override
    public Optional<Account> findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }
    @Override
    public <S extends Account> S save(S entity) {
        return accountRepository.save(entity);
    }

    @Override
    public Account updateAccount(Integer id,  @Valid RegisterRequestForAdmin newAccountRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isPresent()) {
            Account existingAccount = optionalAccount.get();

            if (newAccountRequest.getName() != null) {
                existingAccount.setName(newAccountRequest.getName());
            }

            if (newAccountRequest.getEmail() != null) {
                existingAccount.setEmail(newAccountRequest.getEmail());
            }

            if (newAccountRequest.getPhoneNumber() != null) {
                existingAccount.setPhoneNumber(newAccountRequest.getPhoneNumber());
            }

            if (newAccountRequest.getAddress() != null) {
                existingAccount.setAddress(newAccountRequest.getAddress());
            }
            if (newAccountRequest.getRole() != null) {
                existingAccount.setRole(newAccountRequest.getRole());
            }
            if (newAccountRequest.getStatus() != null) {
                existingAccount.setStatus(newAccountRequest.getStatus());
            }

            return accountRepository.save(existingAccount);
        } else {
            return null;
        }
    }

    @Override
    public void  deleteAccount(Integer id) {
        Account account = accountRepository.findById(id).orElse(null);
        assert account != null;
        account.setStatus(Status.Disable);
        accountRepository.save(account);
    }

    @Override
    public void updateAccountInfo(Account account) {
        accountRepository.save(account);
    }



}