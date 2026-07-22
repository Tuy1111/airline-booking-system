package com.abs.user.application.command;

/** Input for GDPR account erasure (UC: GDPR Account Deletion / Right to be Forgotten). */
public record DeleteAccountCommand(Long userId, String reason) {
}
