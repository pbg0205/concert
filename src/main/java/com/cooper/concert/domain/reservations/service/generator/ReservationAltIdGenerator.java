package com.cooper.concert.domain.reservations.service.generator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.uuid.Generators;

@Component
public class ReservationAltIdGenerator {

	public UUID generateAltId() {
		return Generators.timeBasedEpochGenerator().generate();
	}

}
