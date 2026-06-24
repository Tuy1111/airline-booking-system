package com.abs.user.api.dto;

/** Request body for earning or redeeming frequent-flyer miles. */
public record MilesRequest(long miles, String reason) {
}
