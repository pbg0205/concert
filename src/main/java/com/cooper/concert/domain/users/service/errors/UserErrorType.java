package com.cooper.concert.domain.users.service.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorType {
	USER_NAME_EMPTY(UserErrorCode.ERROR_USER01, "유저 이름을 빈 값일 수 없습니다."),
	USER_NAME_MAX_LENGTH_EXCEEDS(UserErrorCode.ERROR_USER02, "유저 이름의 최대 길이는 100 입니다."),
	USER_BALANCE_NEGATIVE(UserErrorCode.ERROR_USER03, "유저 잔고는 음수일 수 없습니다."),
	USER_NOT_FOUND(UserErrorCode.ERROR_USER04, "유저를 찾을 수 없습니다"),
	USER_BALANCE_NOT_FOUND(UserErrorCode.ERROR_USER05, "유저 잔고를 찾을 수 없습니다."),
	CHARGING_POINT_NEGATIVE(UserErrorCode.ERROR_USER06, "충전 포인트는 음수일 수 없습니다."),
	INSUFFICIENT_BALANCE(UserErrorCode.ERROR_USER07, "유저 잔고가 부족합니다.");

	private final UserErrorCode errorCode;
	private final String message;
}
