package com.example.namo2.global.logging.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.example.namo2.global.logging.wrapper.RequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		if(isAsyncDispatch(request)) {
			filterChain.doFilter(request, response);
		} else {
			doFilterWrapped(
					new RequestWrapper(request),
					new ContentCachingResponseWrapper(response),
					filterChain
			);
		}
	}

	protected void doFilterWrapped(
			RequestWrapper request,
			ContentCachingResponseWrapper response,
			FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
			logRequest(request);
		} finally {
			logResponse(response);
			response.copyBodyToResponse();
		}
	}

	private static void logRequest(
			RequestWrapper request
	) throws IOException {
		String queryString = request.getQueryString();
		String uri = Optional.ofNullable(queryString)
				.map(string -> request.getRequestURI() + string)
				.orElseGet(request::getRequestURI);

		log.info("Request : [{}] /[{}] content-type[{}]",
				request.getMethod(), uri, request.getContentType());

		logPayload("Request",
				request.getContentType(),
				request.getInputStream());
	}

	private static void logResponse(
			ContentCachingResponseWrapper response
	) throws IOException {
		logPayload("Response", response.getContentType(), response.getContentInputStream());
	}

	private static void logPayload(
			String prefix,
			String contentType,
			InputStream inputStream
	) throws IOException {
		MediaType mediaType = MediaType.valueOf(contentType == null ? "application/json" : contentType);
		boolean visible = isVisible(mediaType);

		if (visible) {
			byte[] content = StreamUtils.copyToByteArray(inputStream);
			if (content.length > 0) {
				String contentString = new String(content);
				log.info("{} Payload: {}", prefix, contentString);
			}
		} else {
			log.info("{} Payload: Binary Content", prefix);
		}
	}

	private static boolean isVisible(MediaType mediaType) {
		final List<MediaType> visibleTypes = Arrays.asList(
				MediaType.valueOf("text/*"),
				MediaType.APPLICATION_FORM_URLENCODED,
				MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_XML,
				MediaType.valueOf("application/*+json"),
				MediaType.valueOf("application/*+xml"),
				MediaType.MULTIPART_FORM_DATA
		);

		return visibleTypes.stream()
				.anyMatch(visibleType -> visibleType.includes(mediaType));
	}
}
