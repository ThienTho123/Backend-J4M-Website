package org.example.controller.User;

import lombok.RequiredArgsConstructor;
import org.example.entity.Account;
import org.example.entity.BillInfo;
import org.example.entity.Review;
import org.example.repository.AccountRepository;
import org.example.service.IAccountService;
import org.example.service.IBillInfoService;
import org.example.service.IReviewService;
import org.example.service.Impl.AccountServiceImpl;
import org.example.service.Impl.BillInfoServiceImpl;
import org.example.service.securityService.AuthService;
import org.example.service.securityService.GetIDAccountFromAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {
    private final AccountRepository accountRepository;
    private final IAccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final GetIDAccountFromAuthService getIDAccountService;
    private final IBillInfoService billInfoService;
    private final IReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<Account> getAccountInfo() {
        int idAccount = getIDAccountService.common();
        Account account = accountRepository.findAccountByAccountID(idAccount);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/bought")
    public ResponseEntity<Map<String, Object>> getHistoryBought() {
        int idAccount = getIDAccountService.common();
        List<BillInfo> billInfo = billInfoService.findBillInfoByAccountID(idAccount);
        List<Review> review = reviewService.findReviewByAccountID(idAccount);

        Map<String, Object> response = new HashMap<>();
        response.put("billInfo", billInfo);
        response.put("review", review);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateinfo")
    public ResponseEntity<String> updateAccountInfo(
            @RequestBody Account updateAccountRequest) {

        int idAccount = getIDAccountService.common();
        accountService.updateAccountInfo(
                updateAccountRequest.getName(),
                updateAccountRequest.getPhoneNumber(),
                updateAccountRequest.getAddress(),
                updateAccountRequest.getEmail(),
                idAccount
        );
        return ResponseEntity.ok("Cập nhật thông tin tài khoản thành công.");

    }

    @PostMapping("/changepassword")
    public ResponseEntity<String> updateChangePassword(
            @RequestParam("newpass") String newpass,
            @RequestParam("conpass") String conpass) {

        int idAccount = getIDAccountService.common();

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(conpass, account.getPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không đúng.");
        }

        String updatepass = passwordEncoder.encode(newpass);
        accountRepository.updatePassword(updatepass, idAccount);

        return ResponseEntity.ok("Đổi mật khẩu thành công.");
    }

}
