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

		List<UUID> tokenIds = List.of(
			UUID.fromString("01b8f8a1-6f8c-7b6e-87c3-234a3c15f77e"),
			UUID.fromString("019481e4-cb8e-7a71-8e38-f3eb3d02a058"),
			UUID.fromString("019481e4-f587-7707-8270-593b547d0c4f"),
			UUID.fromString("019481e5-1b7b-77ab-880f-1eb7b59de0e5"),
			UUID.fromString("019481e6-2fc4-786d-a9f5-87bb05390423"),
			UUID.fromString("019481e7-f4d8-7109-8bcf-ccd4b212c3de"),
			UUID.fromString("019481e8-15f6-7968-842a-1be0099d0fea"),
			UUID.fromString("019481e8-49d7-7dc3-9c59-244cfe0b2722"),
			UUID.fromString("019481e8-61ea-7f92-a601-647b2f0317d4"),
			UUID.fromString("019481e8-7b6d-778e-9c0c-9afbdbb0c7ff")
		);

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		// when
		for (int i = 0; i < 10; i++) {
			CompletableFuture<Void> reservationRequestFuture = CompletableFuture.runAsync(
				() -> {
					final Random random = new Random();
					try {
						final int tokenIndex = random.nextInt(10);
						concertReservationUseCase.reserveConcertSeat(tokenIds.get(tokenIndex), seatId);
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
