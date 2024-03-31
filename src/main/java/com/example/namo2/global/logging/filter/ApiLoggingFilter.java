package com.example.namo2.global.logging.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

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
					new ContentCachingRequestWrapper(request),
					new ContentCachingResponseWrapper(response),
					filterChain
			);
		}
	}

	protected void doFilterWrapped(
			ContentCachingRequestWrapper request,
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

	private static void logRequest(ContentCachingRequestWrapper request) throws IOException {
		String queryString = request.getQueryString();
		String s = queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString;
		log.info("Request : {} uri=[{}] content-type[{}]",
				request.getMethod(), s, request.getContentType());

		logPayload("Request", request.getContentType(), request.getContentAsByteArray());
	}

	private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
		logPayload("Response", response.getContentType(), response.getContentAsByteArray());
	}

	private static void logPayload(String prefix, String contentType, byte[] rowData) throws IOException {
		boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));

		ObjectMapper objectMapper = new ObjectMapper();
		Object jsonObject = objectMapper.readValue(new String(rowData), Object.class);
		String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);

		if (visible) {
			if (rowData.length > 0) {
				log.info("{} Payload: {}", prefix, json);
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
