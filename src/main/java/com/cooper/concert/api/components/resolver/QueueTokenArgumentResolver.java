package com.cooper.concert.api.components.resolver;

import java.lang.annotation.Annotation;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.api.components.annotations.QueueToken;
import com.cooper.concert.api.components.dto.TokenHeaderData;
import com.cooper.concert.domain.queues.service.jwt.QueueTokenExtractor;

@RequiredArgsConstructor
public class QueueTokenArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String QUEUE_TOKEN_HEADER_NAME = "QUEUE-TOKEN";
	private final QueueTokenExtractor queueTokenExtractor;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return findMethodAnnotation(QueueToken.class, parameter) != null;
	}

	@Override
	public Object resolveArgument(final MethodParameter parameter,
		final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest,
		final WebDataBinderFactory binderFactory) throws Exception {
		final Long userId = queueTokenExtractor.extractUserIdFromToken(webRequest.getHeader(QUEUE_TOKEN_HEADER_NAME));
		return new TokenHeaderData(userId);
	}

	private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass, MethodParameter parameter) {
		T annotation = parameter.getParameterAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}

		Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
		for (Annotation toSearch : annotationsToSearch) {
			annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(), annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}

		return null;
	}
}
