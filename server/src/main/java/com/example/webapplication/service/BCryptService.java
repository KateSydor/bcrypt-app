package com.example.webapplication.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.webapplication.entity.BCryptEntity;
import com.example.webapplication.repository.BCryptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BCryptService {

    private final BCryptRepository bCryptRepository;

    public BCryptEntity encode(String originalPassword, int rounds) {
        BCryptEntity bCryptEntity = new BCryptEntity();
        bCryptEntity.setOriginalPassword(originalPassword);

        String hashedPassword = BCrypt.withDefaults().hashToString(rounds, originalPassword.toCharArray());
        bCryptEntity.setHashedPassword(hashedPassword);
        // Verify a password against the hashed version
        boolean matches = BCrypt.verifyer().verify(originalPassword.toCharArray(), hashedPassword).verified;
        bCryptEntity.setVerifyResult(matches);
        bCryptEntity.setRounds(rounds);
        return bCryptEntity;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void save(BCryptEntity bCryptEntity) {
        bCryptRepository.saveAndFlush(bCryptEntity);
    }
}
