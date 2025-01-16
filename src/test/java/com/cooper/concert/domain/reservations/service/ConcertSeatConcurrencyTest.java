package com.cooper.concert.domain.reservations.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.ConcurrencyTest;
import com.cooper.concert.base.repository.ReservationTestRepository;
import com.cooper.concert.domain.reservations.models.Reservation;

@ConcurrencyTest
public class ConcertSeatConcurrencyTest {

	@Autowired
	private ConcertReservationService concertReservationService;

	@Autowired
	private ConcertSeatOccupiedCancelService concertSeatOccupiedCancelService;

	@Autowired
	private ReservationTestRepository reservationTestRepository;

	@Test
	@DisplayName("동시 요청에서 좌석 점유에 따른 단일 예약 보장")
	@Sql("classpath:sql/service/concert_seat_concurrency_service.sql")
	void 동시_요청에서_좌석_점유에_따른_단일_예약_보장() {
		// given
		// final Long userId = 1L;
		final Long seatId = 1L;

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(20);

		// when
		for (int i = 0; i < 50; i++) {
			CompletableFuture<Void> reservationRequestFuture = CompletableFuture.runAsync(
				() -> {
					try {
						final Random random = new Random();
						final Long userId = random.nextLong(50) + 1;

						concertReservationService.reserveSeat(userId, seatId);
					} catch (RuntimeException ignored) {}
				}, executorService);

			futures.add(reservationRequestFuture);
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		// then
		final List<Reservation> reservations = reservationTestRepository.findAllBySeatId(seatId);
		assertThat(reservations).hasSize(1);
	}

}
