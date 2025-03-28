package com.cooper.concert.interfaces.schedules.queues.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.service.PaymentCancelService;
import com.cooper.concert.domain.queues.service.ActiveTokenService;
import com.cooper.concert.domain.queues.service.QueueTokenExpiredService;
import com.cooper.concert.domain.queues.service.WaitingQueueService;
import com.cooper.concert.domain.reservations.service.ConcertScheduleSeatsCacheService;
import com.cooper.concert.domain.reservations.service.ConcertSeatOccupiedCancelService;
import com.cooper.concert.domain.reservations.service.ReservationCancelService;
import com.cooper.concert.domain.reservations.service.dto.request.ConcertSeatOccupyCancelRequest;
import com.cooper.concert.domain.reservations.service.dto.response.ConcertSeatOccupyCancelResult;
import com.cooper.concert.domain.reservations.service.dto.response.ReservationCancelResult;
import com.cooper.concert.interfaces.components.annotations.Facade;

@Facade
@RequiredArgsConstructor
@Transactional
public class TokenSchedulerUseCase {

	private final WaitingQueueService waitingQueueService;
	private final ActiveTokenService activeTokenService;
	private final QueueTokenExpiredService queueTokenExpiredService;
	private final ReservationCancelService reservationCancelService;
	private final PaymentCancelService paymentCancelService;
	private final ConcertSeatOccupiedCancelService concertSeatOccupiedCancelService;
	private final ConcertScheduleSeatsCacheService concertScheduleSeatsCacheService;

	public Integer updateToProcessing(final LocalDateTime expiredAt) {
		final Integer availableActiveTokenCount = activeTokenService.countRemainingActiveTokens();
		final List<Long> userIds = waitingQueueService.dequeueUserIds(availableActiveTokenCount);
		return activeTokenService.addActiveTokens(userIds, expiredAt);
	}

	public Integer expireToken(final LocalDateTime expiredAt) {
		final List<Long> expiredTokenUserIds = queueTokenExpiredService.expireExpiredTokens(expiredAt);
		final List<ReservationCancelResult> reservationCancelResults =
			reservationCancelService.cancelReservations(expiredTokenUserIds);

		final List<Long> reservationIds = reservationCancelResults.stream()
			.map(ReservationCancelResult::reservationId)
			.toList();
		paymentCancelService.cancelPayments(reservationIds);

		final List<Long> concertSeatIds = reservationCancelResults.stream()
			.map(ReservationCancelResult::seatId)
			.toList();

		final List<ConcertSeatOccupyCancelResult> concertSeatOccupyCancelResults =
			concertSeatOccupiedCancelService.cancelOccupied(concertSeatIds);

		final List<ConcertSeatOccupyCancelRequest> concertSeatOccupyCancelRequests = concertSeatOccupyCancelResults.stream()
			.map(result -> new ConcertSeatOccupyCancelRequest(result.id(), result.seatNumber(), result.scheduleId()))
			.toList();

		concertScheduleSeatsCacheService.updateToAvailableSeats(concertSeatOccupyCancelRequests);

		return expiredTokenUserIds.size();
	}

}
