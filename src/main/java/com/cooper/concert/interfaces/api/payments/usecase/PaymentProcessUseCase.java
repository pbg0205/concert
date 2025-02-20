package com.cooper.concert.interfaces.api.payments.usecase;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.service.PaymentProcessingService;
import com.cooper.concert.domain.payments.service.dto.response.PaymentCompleteInfo;
import com.cooper.concert.domain.payments.service.dto.response.PaymentProcessResult;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenOutboxEvent;
import com.cooper.concert.domain.reservations.service.ConcertReservationService;
import com.cooper.concert.domain.reservations.service.ConcertSeatReadService;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertReservationCompletedInfo;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatPriceInfo;
import com.cooper.concert.domain.users.service.UserBalanceUseService;
import com.cooper.concert.interfaces.components.annotations.Facade;

@Facade
@RequiredArgsConstructor
@Transactional
public class PaymentProcessUseCase {

	private final ConcertReservationService concertReservationService;
	private final ConcertSeatReadService concertSeatReadService;
	private final UserBalanceUseService userBalanceUseService;
	private final PaymentProcessingService paymentProcessingService;
	private final ApplicationEventPublisher applicationEventPublisher;

	public PaymentProcessResult processPayment(final UUID paymentAltId, final Long userId) {
		final PaymentCompleteInfo paymentCompleteInfo = paymentProcessingService.completePayment(paymentAltId);

		final QueueTokenExpireEvent queueTokenExpireEvent = new QueueTokenExpireEvent(userId, paymentAltId);
		applicationEventPublisher.publishEvent(new QueueTokenOutboxEvent(paymentAltId, queueTokenExpireEvent));

		final ConcertReservationCompletedInfo reservationCompletedInfo =
			concertReservationService.completeReservation(paymentCompleteInfo.reservationId());

		final ConcertSeatPriceInfo concertSeatPriceInfo =
			concertSeatReadService.findConcertSeatReadInfoById(reservationCompletedInfo.seatId());

		userBalanceUseService.usePoint(reservationCompletedInfo.userId(), concertSeatPriceInfo.price());

		applicationEventPublisher.publishEvent(queueTokenExpireEvent);

		return new PaymentProcessResult(reservationCompletedInfo.altId());
	}
}
