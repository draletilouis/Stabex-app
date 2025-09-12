package com.demo.banking_app.interceptor;

import com.demo.banking_app.util.ApiVersionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to handle API versioning
 * Validates API versions and adds version information to request attributes
 */
@Component
@Slf4j
public class ApiVersionInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String requestUri = request.getRequestURI();
        
        // Skip versioning for non-API requests
        if (!requestUri.startsWith("/api/")) {
            return true;
        }
        
        // Extract version from request
        String version = ApiVersionUtil.extractVersionFromRequest(request);
        
        // Validate version
        if (!ApiVersionUtil.isVersionSupported(version)) {
            log.warn("Unsupported API version: {} for request: {}", version, requestUri);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            try {
                response.getWriter().write(String.format(
                    "{\"error\":\"Unsupported API version\",\"version\":\"%s\",\"supportedVersions\":[\"1\",\"2\"]}", 
                    version));
            } catch (Exception e) {
                log.error("Error writing error response", e);
            }
            return false;
        }
        
        // Add version to request attributes for use in controllers
        request.setAttribute("apiVersion", version);
        request.setAttribute("apiVersionMajor", getMajorVersion(version));
        
        log.debug("API version {} detected for request: {}", version, requestUri);
        return true;
    }
    
    /**
     * Extract major version number from version string
     */
    private String getMajorVersion(String version) {
        if (version == null || version.isEmpty()) {
            return "1";
        }
        
        int dotIndex = version.indexOf('.');
        if (dotIndex > 0) {
            return version.substring(0, dotIndex);
        }
        
        return version;
    }
}
