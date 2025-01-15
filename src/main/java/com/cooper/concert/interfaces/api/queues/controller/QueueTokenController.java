package com.cooper.concert.interfaces.api.queues.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.support.response.ApiResponse;
import com.cooper.concert.domain.queues.service.dto.QueueTokenIssueResult;
import com.cooper.concert.interfaces.api.queues.dto.request.QueueTokenIssueRequest;
import com.cooper.concert.interfaces.api.queues.dto.response.QueueTokenIssueResponse;
import com.cooper.concert.interfaces.api.queues.usecase.QueueTokenIssueUseCase;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueTokenController {

	private final QueueTokenIssueUseCase queueTokenIssueUseCase;

	@PostMapping("/token/issue")
	public ResponseEntity<ApiResponse<QueueTokenIssueResponse>> generateToken(
		@RequestBody final QueueTokenIssueRequest queueTokenIssueRequest) {

		final QueueTokenIssueResult queueTokenIssueResult =
			queueTokenIssueUseCase.issueQueueToken(queueTokenIssueRequest.getUserId());

		final QueueTokenIssueResponse queueTokenIssueResponse = new QueueTokenIssueResponse(
			queueTokenIssueResult.tokenId(),
			queueTokenIssueResult.waitingPosition());

		return ResponseEntity.ok(ApiResponse.success(queueTokenIssueResponse));
	}
}
