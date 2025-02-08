package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@Service
@RequiredArgsConstructor
public class ActiveTokenService {

	@Value("${queue.processing.capacity}")
	private Integer processingTokenCapacity;

	private final ActiveTokenRepository activeTokenRepository;

	public Integer addActiveTokens(List<Long> userIds, final LocalDateTime expiredAt) {
		return activeTokenRepository.addActiveQueueTokens(userIds, expiredAt);
	}

	public Integer countRemainingActiveTokens() {
		final Integer activeTokens = activeTokenRepository.countActiveTokens();
		return processingTokenCapacity - activeTokens;
	}

}
