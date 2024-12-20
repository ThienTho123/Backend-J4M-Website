package org.example.repository;

import org.example.entity.Account;
import org.example.entity.BillInfo;
import org.example.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillInfoRepository extends JpaRepository<BillInfo, Integer> {
    List<BillInfo> findBillInfoByBillID_AccountID_AccountIDAndStatus(int id, Status status);
}
