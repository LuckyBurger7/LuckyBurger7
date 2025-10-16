package org.example.luckyburger.common.security.dto;

import org.example.luckyburger.domain.auth.enums.AccountRole;

public record AuthAccount(Long accountId, String email, AccountRole role) {
}
