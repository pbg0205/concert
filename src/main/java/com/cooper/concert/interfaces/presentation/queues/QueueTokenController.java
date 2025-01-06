package com.cooper.concert.interfaces.presentation.queues;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooper.concert.common.api.support.response.ApiResponse;
import com.cooper.concert.interfaces.presentation.queues.dto.request.QueueTokenIssueRequest;
import com.cooper.concert.interfaces.presentation.queues.dto.response.QueueTokenIssueResponse;

@RestController
@RequestMapping("/api/queue")
public class QueueTokenController {

	@PostMapping("/token/issue")
	public ResponseEntity<ApiResponse<QueueTokenIssueResponse>> generateToken(
		@RequestBody final QueueTokenIssueRequest queueTokenIssueRequest) {
		return ResponseEntity.ok(ApiResponse.success(new QueueTokenIssueResponse(UUID.randomUUID(), 10)));
	}
}
