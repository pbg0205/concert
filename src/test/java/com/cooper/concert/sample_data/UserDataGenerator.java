package com.cooper.concert.sample_data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.uuid.Generators;

public class UserDataGenerator {
	public static void main(String[] args) {
		int totalUsers = 100_000;  // 총 10만 명의 사용자 생성
		long startTimestamp = 1672972800L;  // 예시 타임스탬프 (초 단위, 2023-01-06 00:00:00 UTC 기준)

		try (
			BufferedWriter userWriter = new BufferedWriter(new FileWriter("sample-data/user_small.csv"));
			BufferedWriter balanceWriter = new BufferedWriter(new FileWriter("sample-data/user_balance_small.csv"));
			BufferedWriter altIdWriter = new BufferedWriter(new FileWriter("k6/user_alt_id.csv"))
		) {
			// user 테이블 CSV 헤더
			userWriter.write("id,name,alt_id,created_at,modified_at\n");
			// user_balance 테이블 CSV 헤더
			balanceWriter.write("id,user_id,point,created_at,modified_at\n");

			int balanceId = 1;  // user_balance의 id는 1부터 시작

			// 사용자 및 user_balance 데이터 생성
			for (int userId = 1; userId <= totalUsers; userId++) {
				String name = "user" + String.format("%05d", userId);  // 예: user00001
				final String altIdStrVal = Generators.timeBasedEpochGenerator().generate().toString();
				String altId = convertUuidToHex(altIdStrVal);  // UUID -> 16진수 문자열 변환
				long createdAt = startTimestamp + userId;  // 각 사용자마다 타임스탬프 1초씩 증가

				// user 데이터 작성
				userWriter.write(String.format("%d,%s,%s,%d,%d\n", userId, name, altId, createdAt, createdAt));

				// user_balance 데이터 작성 (포인트는 고정 1000)
				balanceWriter.write(String.format("%d,%d,1000,%d,%d\n", balanceId++, userId, createdAt, createdAt));

				// alt_id 데이터 작성
				altIdWriter.write(String.format("%s\n", altIdStrVal));  // alt_id만 기록
			}

			System.out.println("Generated user_small.csv and user_balance_small.csv with 100,000 users.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convertUuidToHex(String uuid) {
		return uuid.replace("-", "");
	}
}
