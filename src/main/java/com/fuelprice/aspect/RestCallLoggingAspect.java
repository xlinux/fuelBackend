package com.fuelprice.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RestCallLoggingAspect {

	private static final Logger log = LogManager.getLogger(RestCallLoggingAspect.class);

	@Around("""
		    within(@org.springframework.web.bind.annotation.RestController *)
		    && !execution(* com.fuelprice.controller.*Import*.*(..))
		    && !execution(* com.fuelprice.controller.MimitImportController.*(..))
		""")
		public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {

		long start = System.currentTimeMillis();

		HttpServletRequest request = currentRequest();

		String method = request != null ? request.getMethod() : "-";
		String uri = request != null ? request.getRequestURI() : "-";
		String query = request != null ? request.getQueryString() : null;
		String deviceId = request != null ? request.getHeader("X-Device-Id") : null;

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String controllerMethod = signature.getDeclaringType().getSimpleName() + "." + signature.getMethod().getName();

		try {
			log.info("REST START method={} uri={} query={} deviceId={} handler={}", method, uri, query, mask(deviceId),
					controllerMethod);

			Object result = joinPoint.proceed();

			long elapsed = System.currentTimeMillis() - start;

			log.info("REST END method={} uri={} handler={} elapsedMs={}", method, uri, controllerMethod, elapsed);

			return result;

		} catch (Exception ex) {
			long elapsed = System.currentTimeMillis() - start;

			log.error("REST ERROR method={} uri={} handler={} elapsedMs={} error={}", method, uri, controllerMethod,
					elapsed, ex.getMessage(), ex);

			throw ex;
		}
	}

	private HttpServletRequest currentRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

		return attrs != null ? attrs.getRequest() : null;
	}

	private String mask(String value) {
		if (value == null || value.length() < 8) {
			return value;
		}

		return value.substring(0, 4) + "****" + value.substring(value.length() - 4);
	}
}