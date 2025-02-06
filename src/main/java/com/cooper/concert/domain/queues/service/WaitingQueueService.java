package com.cooper.concert.domain.queues.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.repository.WaitingQueueRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitingQueueService {

	private final WaitingQueueRepository waitingQueueRepository;

	public boolean existQueueToken(final Long userId) {
		return waitingQueueRepository.existsUserId(userId);
	}

	public boolean removeFromWaitingQueue(final Long userId) {
		return waitingQueueRepository.removeUserId(userId);
	}

	public Long enqueueUserId(final Long userId) {
		return waitingQueueRepository.enqueueUserId(userId);
	}

	public List<Long> dequeueUserIds(final Integer availableActiveTokenCount) {
		return waitingQueueRepository.dequeueUserIds(availableActiveTokenCount);
	}
}
