package com.cooper.concert.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.concert.base.annotations.ConcurrencyTest;
import com.cooper.concert.base.repository.UserBalanceTestRepository;
import com.cooper.concert.domain.users.models.UserBalance;
import com.cooper.concert.domain.users.service.UserBalanceChargeService;
import com.cooper.concert.domain.users.service.UserBalanceUseService;

@ConcurrencyTest
public class UserBalanceConcurrencyTest {

	@Autowired
	private UserBalanceUseService userBalanceUseService;

	@Autowired
	private UserBalanceChargeService userBalanceChargeService;

	@Autowired
	private UserBalanceTestRepository userBalanceTestRepository;

	@Test
	@DisplayName("포인트 충전에 대한 동시성 제어")
	@Sql("classpath:sql/concurrency/user_balance_charge_concurrency_service.sql")
	void 포인트_충전에_대한_동시성_제어 () throws InterruptedException {
	    // given
		final Long userId = 1L;
		final Long point = 1000L;

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(20);

		// when
		for (int i = 0; i < 100; i++) {
			addPointChargeFuture(userId, point, executorService, futures);
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		executorService.shutdown();

	    // then
		final UserBalance userBalance = userBalanceTestRepository.findUserBalanceByUserId(userId);
		assertThat(userBalance).extracting("point").isEqualTo(100_000L);
	}

	@Test
	@DisplayName("포인트 사용에 대한 동시성 제어")
	@Sql("classpath:sql/concurrency/user_balance_charge_concurrency_service.sql")
	void 포인트_사용에_대한_동시성_제어_보장 () throws InterruptedException {
	    // given
		final Long userId = 2L;
		final Long point = 1000L;

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(20);

		// when
		for (int i = 0; i < 100; i++) {
			addPointUseFuture(userId, point, executorService, futures);
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		executorService.shutdown();

	    // then
		final UserBalance userBalance = userBalanceTestRepository.findUserBalanceByUserId(userId);
		assertThat(userBalance).extracting("point").isEqualTo(0L);
	}

	@Test
	@DisplayName("포인트 사용과 충전 대한 동시성 제어 보장")
	@Sql("classpath:sql/concurrency/user_balance_charge_concurrency_service.sql")
	void 포인트_사용과_충전_대한_동시성_제어 () throws InterruptedException {
		// given
		final Long userId = 3L;
		final Long point = 1000L;

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(20);

		// when
		for (int i = 0; i < 100; i++) {
			addPointChargeFuture(userId, point, executorService, futures);
			addPointUseFuture(userId, point, executorService, futures);
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		executorService.shutdown();

		// then
		final UserBalance userBalance = userBalanceTestRepository.findUserBalanceByUserId(userId);
		assertThat(userBalance).extracting("point").isEqualTo(100_000L);
	}

	private void addPointChargeFuture(final Long userId, final Long point, final ExecutorService executorService,
		final List<CompletableFuture<Void>> futures) {
		CompletableFuture<Void> chargeFuture = CompletableFuture.runAsync(
			() -> userBalanceChargeService.chargePoint(userId, point), executorService);

		futures.add(chargeFuture);
	}

	private void addPointUseFuture(final Long userId, final Long point, final ExecutorService executorService,
		final List<CompletableFuture<Void>> futures) {
		CompletableFuture<Void> pointUseFuture = CompletableFuture.runAsync(
			() -> userBalanceUseService.usePoint(userId, point), executorService);

		futures.add(pointUseFuture);
	}

}
