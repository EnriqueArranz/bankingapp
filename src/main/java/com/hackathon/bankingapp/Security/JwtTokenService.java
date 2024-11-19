package com.hackathon.bankingapp.Security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenService {

    private Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        if (token == null) {
            return false;
        }
        return blacklistedTokens.contains(token);
    }
}