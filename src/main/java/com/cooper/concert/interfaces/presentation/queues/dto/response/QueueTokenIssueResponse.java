package com.cooper.concert.interfaces.presentation.queues.dto.response;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class QueueTokenIssueResponse {
	private final UUID token;
	private final Integer waitingPosition;
}
