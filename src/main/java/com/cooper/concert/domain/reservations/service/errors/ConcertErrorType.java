package com.cooper.concert.domain.reservations.service.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcertErrorType {
	INVALID_CONCERT_SCHEDULE(ConcertErrorCode.ERROR_CONCERT01, "콘서트 종료 일자는 시작 일자보다 앞설 수 없습니다."),
	CONCERT_NAME_EMPTY(ConcertErrorCode.ERROR_CONCERT02, "콘서트 이름은 빈칸일 수 없습니다."),
	CONCERT_NOT_FOUND(ConcertErrorCode.ERROR_CONCERT03, "콘서트를 조회할 수 없습니다.");

	private final ConcertErrorCode errorCode;
	private final String message;
}
