package com.cooper.concert.domain.payments.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.uuid.Generators;

@Component
public class PaymentAltIdGenerator {

	public UUID generatePaymentAltId() {
		return Generators.timeBasedEpochGenerator().generate();
	}
}
