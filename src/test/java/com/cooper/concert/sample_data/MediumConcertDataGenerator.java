package com.cooper.concert.sample_data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MediumConcertDataGenerator {

	public static void main(String[] args) {
		int totalConcerts = 1000; // 총 1000개의 콘서트 데이터
		int scheduleMultiplier = 3; // 콘서트 1개당 3개의 스케줄
		int seatMultiplier = 1000; // 스케줄 1개당 1000개의 좌석

		// 파일을 생성할 준비
		try (
			BufferedWriter concertWriter = new BufferedWriter(new FileWriter("sample-data/concert_medium.csv"));
			BufferedWriter scheduleWriter = new BufferedWriter(new FileWriter("sample-data/concert_schedule_medium.csv"));
			BufferedWriter seatWriter = new BufferedWriter(new FileWriter("sample-data/concert_seat_medium.csv"))
		) {
			// 콘서트 데이터 CSV 헤더
			concertWriter.write("id,concert_id,name,created_at,modified_at\n");

			// 스케줄 데이터 CSV 헤더
			scheduleWriter.write("id,concert_id,start_at,end_at,created_at,modified_at\n");

			// 좌석 데이터 CSV 헤더 (id 추가)
			seatWriter.write("id,schedule_id,seat_number,status,price,created_at,modified_at\n");

			// 콘서트 데이터를 생성 (콘서트 ID는 100,001부터 시작)
			for (int concertId = 100001; concertId < 100001 + totalConcerts; concertId++) {
				long createdAt = 1752573600000L + (concertId * 86400000L); // 시작일에서 1일씩 증가
				concertWriter.write(String.format("%d,%d,Concert %d,%d,%d\n", concertId, concertId, concertId, createdAt, createdAt));
			}

			// 스케줄 데이터를 생성 (각 콘서트당 3개의 스케줄)
			for (int concertId = 100001; concertId < 100001 + totalConcerts; concertId++) {
				long createdAt = 1752573600000L + (concertId * 86400000L); // 콘서트와 동일한 시작 날짜
				for (int scheduleOffset = 0; scheduleOffset < scheduleMultiplier; scheduleOffset++) {
					long startAt = createdAt + (scheduleOffset * 7200000L); // 2시간 간격으로 시작 시간
					long endAt = startAt + 7200000L; // 2시간 공연 종료 시간
					scheduleWriter.write(String.format("%d,%d,%d,%d,%d,%d\n", concertId * 10 + scheduleOffset + 1, concertId, startAt, endAt, createdAt, createdAt));
				}
			}

			// 좌석 데이터를 생성 (각 스케줄당 1000개의 좌석)
			int seatId = 10000001; // 좌석 ID는 10,000,001부터 시작
			for (int concertId = 100001; concertId < 100001 + totalConcerts; concertId++) {
				for (int scheduleOffset = 0; scheduleOffset < scheduleMultiplier; scheduleOffset++) {
					int scheduleId = concertId * 10 + scheduleOffset + 1;
					long createdAt = 1752573600000L + (concertId * 86400000L); // 동일한 생성일
					for (int seatNumber = 1; seatNumber <= seatMultiplier; seatNumber++) {
						seatWriter.write(String.format("%d,%d,%d,AVAILABLE,1000,%d,%d\n", seatId, scheduleId, seatNumber, createdAt, createdAt));
						seatId++; // 좌석마다 id 증가
					}
				}
			}

			System.out.println("Generated concert_middle.csv, concert_schedule_middle.csv, concert_seat_middle.csv");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
