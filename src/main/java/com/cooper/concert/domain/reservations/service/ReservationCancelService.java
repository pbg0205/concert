package com.cooper.concert.domain.reservations.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.models.Reservation;
import com.cooper.concert.domain.reservations.models.ReservationStatus;
import com.cooper.concert.domain.reservations.service.dto.response.ReservationCancelResult;
import com.cooper.concert.domain.reservations.service.repository.ReservationCommandRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCancelService {

	private final ReservationCommandRepository reservationCommandRepository;

	public List<ReservationCancelResult> cancelReservations(List<Long> expiredTokenUserIds) {
		final ReservationStatus reservationStatus = ReservationStatus.PENDING;
		final List<Reservation> reservations =
			reservationCommandRepository.findByUserIdsAndReservationStatus(expiredTokenUserIds, reservationStatus.name());

		return reservations.stream()
			.filter(Reservation::cancel)
			.map(reservation -> new ReservationCancelResult(reservation.getId(), reservation.getSeatId()))
			.toList();
	}
}
