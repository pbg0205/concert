package com.cooper.concert.common.api.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorMessage {
	private final String code;
	private final String message;
	private final Object data;

	public ErrorMessage(final String code, final String message) {
		this.code = code;
		this.message = message;
		this.data = null;
	}

}
