package com.cooper.concert.domain.reservations.infrastructure.rdb;

import static com.cooper.concert.domain.reservations.models.QReservation.reservation;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.reservations.models.Reservation;
import com.cooper.concert.domain.reservations.service.repository.ReservationCommandRepository;

@Repository
@RequiredArgsConstructor
public class ReservationCommandRepositoryDsl implements ReservationCommandRepository {

	private final JpaReservationRepository reservationRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public Reservation save(final Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Override
	public Reservation findById(final Long id) {
		return queryFactory.selectFrom(reservation)
			.where(reservation.id.eq(id))
			.fetchOne();
	}

	@Override
	public List<Reservation> findByUserIdsAndReservationStatus(final List<Long> userIds,
		final String reservationStatus) {
		return queryFactory.selectFrom(reservation)
			.where(reservation.userId.in(userIds).and(reservation.status.stringValue().eq(reservationStatus)))
			.fetch();
	}
}
