package com.abs.user.application.command;

/** Input for redeeming frequent-flyer miles (UC: Manage Frequent-Flyer Tier). */
public record RedeemMilesCommand(Long userId, long miles, String reason) {
}
