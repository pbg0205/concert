package com.cooper.concert.interfaces.api.reservations.usecase;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.service.PaymentProcessingService;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCreationInfo;
import com.cooper.concert.domain.queues.service.QueueTokenReadService;
import com.cooper.concert.domain.reservations.service.ConcertReservationService;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationResult;
import com.cooper.concert.interfaces.components.annotations.Facade;
import com.cooper.concert.storage.redis.redisson.components.annotations.DistributedLock;

@Facade
@RequiredArgsConstructor
@Transactional
public class ConcertReservationUseCase {

	private final QueueTokenReadService queueTokenReadService;
	private final ConcertReservationService concertReservationService;
	private final PaymentProcessingService paymentProcessingService;

	@DistributedLock(key = "'SEAT:' + #seatId")
	public ConcertReservationResult reserveConcertSeat(final UUID tokenId, final Long seatId) {
		final Long userId = queueTokenReadService.findUserIdByTokenId(tokenId);
		final ConcertReservationInfo concertReservationInfo = concertReservationService.reserveSeat(userId, seatId);
		final PaymentCreationInfo paymentCreationInfo =
			paymentProcessingService.createPendingPayment(concertReservationInfo.reservationId());

		return new ConcertReservationResult(
			concertReservationInfo.reservationAltId(), paymentCreationInfo.paymentAltId());
	}
}
