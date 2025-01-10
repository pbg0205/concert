package com.cooper.concert.common.api.resolver;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.UUID;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cooper.concert.common.api.annotations.QueueToken;

public class QueueTokenArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String QUEUE_TOKEN_HEADER_NAME = "QUEUE-TOKEN";

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return findMethodAnnotation(QueueToken.class, parameter) != null;
	}

	@Override
	public Object resolveArgument(final MethodParameter parameter,
		final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest,
		final WebDataBinderFactory binderFactory) throws Exception {

		String tokenId = webRequest.getHeader(QUEUE_TOKEN_HEADER_NAME);
		return UUID.fromString(Objects.requireNonNull(tokenId));
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
