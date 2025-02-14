package com.cooper.concert.sample_data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SmallConcertDataGenerator {

	public static void main(String[] args) {
		int totalConcerts = 100000; // 콘서트 데이터 개수
		int scheduleMultiplier = 1; // 콘서트 1개 당 스케줄 1개
		int seatMultiplier = 100;  // 스케줄 1개 당 좌석 100개

		// 파일을 생성할 준비
		try (
			BufferedWriter concertWriter = new BufferedWriter(
				new FileWriter("sample-data/concert_small.csv"));
			BufferedWriter scheduleWriter = new BufferedWriter(
				new FileWriter("sample-data/concert_schedule_small.csv"));
			BufferedWriter seatWriter = new BufferedWriter(
				new FileWriter("sample-data/concert_seat_small.csv"))
		) {
			// 콘서트 데이터 CSV 헤더
			concertWriter.write("id,concert_id,name,created_at,modified_at\n");

			// 스케줄 데이터 CSV 헤더
			scheduleWriter.write("id,concert_id,start_at,end_at,created_at,modified_at\n");

			// 좌석 데이터 CSV 헤더 (id 추가)
			seatWriter.write("id,schedule_id,seat_number,status,price,created_at,modified_at\n");

			// 콘서트 데이터를 생성
			for (int concertId = 1; concertId <= totalConcerts; concertId++) {
				long createdAt = 1752573600000L + (concertId * 86400000L); // 시작일에서 1일씩 증가
				concertWriter.write(
					String.format("%d,%d,Concert %d,%d,%d\n", concertId, concertId, concertId, createdAt, createdAt));
			}

			// 스케줄 데이터를 생성
			for (int concertId = 1; concertId <= totalConcerts; concertId++) {
				long createdAt = 1752573600000L + (concertId * 86400000L); // 콘서트와 동일한 시작 날짜
				long startAt = createdAt; // 스케줄 시작 시간
				long endAt = startAt + 7200000L; // 2시간 후 종료 시간
				scheduleWriter.write(
					String.format("%d,%d,%d,%d,%d,%d\n", concertId, concertId, startAt, endAt, createdAt, createdAt));
			}

			// 좌석 데이터를 생성 (id 추가)
			int seatId = 1; // 좌석 id는 1부터 시작
			for (int concertId = 1; concertId <= totalConcerts; concertId++) {
				for (int scheduleId = (concertId - 1) * scheduleMultiplier + 1; scheduleId <= concertId; scheduleId++) {
					long createdAt = 1752573600000L + (concertId * 86400000L); // 동일한 생성일
					for (int seatNumber = 1; seatNumber <= seatMultiplier; seatNumber++) {
						seatWriter.write(
							String.format("%d,%d,%d,AVAILABLE,1000,%d,%d\n", seatId, scheduleId, seatNumber, createdAt,
								createdAt));
						seatId++; // 좌석마다 id 증가
					}
				}
			}

			System.out.println("Generated concert_small.csv, concert_schedule_small.csv, concert_seat_small.csv");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
