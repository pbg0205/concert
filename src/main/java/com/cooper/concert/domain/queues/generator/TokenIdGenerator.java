package com.cooper.concert.domain.queues.generator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.uuid.Generators;

@Component
public class TokenIdGenerator {

	public UUID generateTokenId() {
		return Generators.timeBasedEpochGenerator().generate();
	}

}
