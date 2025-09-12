package com.demo.banking_app.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for API versioning
 * Provides methods to extract and validate API versions from requests
 */
public class ApiVersionUtil {
    
    private static final String DEFAULT_VERSION = "1";
    private static final Pattern VERSION_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");
    private static final Pattern PATH_VERSION_PATTERN = Pattern.compile("/v(\\d+(?:\\.\\d+)?)/");
    private static final Pattern ACCEPT_VERSION_PATTERN = Pattern.compile("version=([^,;\\s]+)");
    
    /**
     * Extract API version from request using multiple strategies
     * Priority: Header > URL Path > Accept Header > Default
     */
    public static String extractVersionFromRequest(HttpServletRequest request) {
        // Strategy 1: Check X-API-Version header (highest priority)
        String headerVersion = extractVersionFromHeader(request);
        if (headerVersion != null) {
            return headerVersion;
        }
        
        // Strategy 2: Check URL path
        String pathVersion = extractVersionFromPath(request);
        if (pathVersion != null) {
            return pathVersion;
        }
        
        // Strategy 3: Check Accept header
        String acceptVersion = extractVersionFromAcceptHeader(request);
        if (acceptVersion != null) {
            return acceptVersion;
        }
        
        // Strategy 4: Default version
        return DEFAULT_VERSION;
    }
    
    /**
     * Extract version from X-API-Version header
     */
    public static String extractVersionFromHeader(HttpServletRequest request) {
        String headerVersion = request.getHeader("X-API-Version");
        return validateVersion(headerVersion);
    }
    
    /**
     * Extract version from URL path (e.g., /api/v1/users -> "1")
     */
    public static String extractVersionFromPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        Matcher matcher = PATH_VERSION_PATTERN.matcher(requestPath);
        
        if (matcher.find()) {
            return validateVersion(matcher.group(1));
        }
        
        return null;
    }
    
    /**
     * Extract version from Accept header
     * Handles: application/json;version=1, application/xml;charset=utf-8;version=2.1
     */
    public static String extractVersionFromAcceptHeader(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        
        if (acceptHeader == null || acceptHeader.trim().isEmpty()) {
            return null;
        }
        
        Matcher matcher = ACCEPT_VERSION_PATTERN.matcher(acceptHeader);
        if (matcher.find()) {
            return validateVersion(matcher.group(1));
        }
        
        return null;
    }
    
    /**
     * Validate version format and return if valid, null otherwise
     */
    public static String validateVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            return null;
        }
        
        version = version.trim();
        
        if (VERSION_PATTERN.matcher(version).matches()) {
            return version;
        }
        
        return null;
    }
    
    /**
     * Check if version is supported
     */
    public static boolean isVersionSupported(String version) {
        if (version == null) {
            return false;
        }
        
        // Support versions 1 and 2
        return "1".equals(version) || "2".equals(version) || 
               "1.0".equals(version) || "2.0".equals(version);
    }
    
    /**
     * Get the latest supported version
     */
    public static String getLatestVersion() {
        return "2";
    }
    
    /**
     * Get the default version
     */
    public static String getDefaultVersion() {
        return DEFAULT_VERSION;
    }
}
