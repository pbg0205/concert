package com.cooper.concert.interfaces.presentation.users.dto.request;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class UserBalanceReChargeRequest {
	private final UUID userId;
	private final Long point;
}
