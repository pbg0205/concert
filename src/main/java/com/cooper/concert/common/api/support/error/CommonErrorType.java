package com.cooper.concert.common.api.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorType {
	ERROR_UNKNOWN(CommonErrorCode.ERROR_COMMON01, "알 수 없는 예외가 발생했습니다."),
	ERROR_MESSAGE_UNREADABLE(CommonErrorCode.ERROR_COMMON02, "읽을 수 없는 HTTP 메시지 입니다.");

	private final CommonErrorCode errorCode;
	private final String message;
}
