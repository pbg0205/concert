package com.cooper.concert.interfaces.consumers.reservations;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.service.ConcertScheduleSeatsCacheService;
import com.cooper.concert.domain.reservations.service.dto.event.SeatCacheUnavailableEvent;

@Component
@RequiredArgsConstructor
public class CachingSeatEventListener {

	private final ConcertScheduleSeatsCacheService concertScheduleSeatsCacheService;

	@EventListener
	public void handleQueueTokenExpire(SeatCacheUnavailableEvent seatCacheUnavailableEvent) {
		concertScheduleSeatsCacheService.updateToUnAvailableSeat(
			seatCacheUnavailableEvent.scheduleId(),
			seatCacheUnavailableEvent.seatId(),
			seatCacheUnavailableEvent.seatNumber());
	}
}
