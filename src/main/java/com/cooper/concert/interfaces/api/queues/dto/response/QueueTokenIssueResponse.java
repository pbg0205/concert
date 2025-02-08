package com.cooper.concert.interfaces.api.queues.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class QueueTokenIssueResponse {
	private final String token;
	private final Long position;
}
