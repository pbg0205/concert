package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.ActiveTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QueueTokenExpiredService {

	private final ActiveTokenRepository activeTokenRepository;

	public List<Long> expireExpiredTokens(final LocalDateTime expiredAt) {
		return activeTokenRepository.removeAllLteExpiredAt(expiredAt);
	}

	public boolean expireCompleteToken(final Long userId) {
		if (activeTokenRepository.removeActiveToken(userId)) {
			return true;
		} else {
			throw new TokenNotFoundException(TokenErrorType.TOKEN_NOT_FOUND);
		}
	}
}
