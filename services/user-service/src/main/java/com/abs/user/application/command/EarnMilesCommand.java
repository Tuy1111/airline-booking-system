package com.abs.user.application.command;

/** Input for crediting frequent-flyer miles (UC: Manage Frequent-Flyer Tier). */
public record EarnMilesCommand(Long userId, long miles, String reason) {
}
