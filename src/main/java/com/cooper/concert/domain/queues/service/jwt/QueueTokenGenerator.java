package com.cooper.concert.domain.queues.service.jwt;

import java.time.Instant;

public interface QueueTokenGenerator {
	String generateJwt(Long userId, Instant issuedAt);
}
