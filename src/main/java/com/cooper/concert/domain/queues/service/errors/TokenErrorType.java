package com.cooper.concert.domain.queues.service.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorType {

	TOKEN_NOT_FOUND(TokenErrorCode.ERROR_TOKEN01, "토큰이 존재하지 않습니다."),
	PROCESSING_TOKEN_INVALID(TokenErrorCode.ERROR_TOKEN02, "활성화 토큰이 아닙니다.");

	private final TokenErrorCode errorCode;
	private final String message;
}
