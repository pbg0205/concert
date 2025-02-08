package com.cooper.concert.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import com.cooper.concert.interfaces.api.reservations.usecase.ConcertReservationUseCase;

@ConcurrencyTest
public class ConcertSeatConcurrencyTest {

	@Autowired
	private ConcertReservationUseCase concertReservationUseCase;

	@Autowired
	private ReservationTestRepository reservationTestRepository;

	@Test
	@DisplayName("동시 요청에서 좌석 점유에 따른 단일 예약 보장")
	@Sql("classpath:sql/concurrency/concert_seat_concurrency_usecase.sql")
	void 동시_요청에서_좌석_점유에_따른_단일_예약_보장() {
		// given
		final Long seatId = 1L;

		List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		// when
		for (int i = 0; i < 10; i++) {
			CompletableFuture<Void> reservationRequestFuture = CompletableFuture.runAsync(
				() -> {
					final Random random = new Random();
					try {
						final int userIndex = random.nextInt(10);
						concertReservationUseCase.reserveConcertSeat(userIds.get(userIndex), seatId);
					} catch (RuntimeException ignored) {
					}
				}, executorService);

			futures.add(reservationRequestFuture);
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		// then
		final List<Reservation> reservations = reservationTestRepository.findAllBySeatId(seatId);
		assertThat(reservations).hasSize(1);
	}

}
