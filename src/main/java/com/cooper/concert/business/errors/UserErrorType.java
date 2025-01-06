package com.cooper.concert.business.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorType {
	USER_NAME_EMPTY(UserErrorCode.ERROR_USER01, "유저 이름을 빈 값일 수 없습니다."),
	USER_NAME_MAX_LENGTH_EXCEEDS(UserErrorCode.ERROR_USER02, "유저 이름의 최대 길이는 100 입니다.");

	private final UserErrorCode errorCode;
	private final String message;
}
