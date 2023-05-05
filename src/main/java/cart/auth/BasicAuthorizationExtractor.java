package cart.auth;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthorizationExtractor implements AuthorizationExtractor<AuthInfo> {
    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";
    public static final int AUTH_INFO_SIZE = 2;

    @Override
    public AuthInfo extract(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (header == null) {
            return null;
        }

        if ((header.toLowerCase().startsWith(BASIC_TYPE.toLowerCase()))) {
            String[] credentials = extractCredentials(header);

            return getAuthInfo(credentials);
        }
        return null;
    }

    private static AuthInfo getAuthInfo(String[] credentials) {
        if (credentials.length < AUTH_INFO_SIZE) {
            return null;
        }
        String email = credentials[0];
        String password = credentials[1];
        return new AuthInfo(email, password);
    }

    private static String[] extractCredentials(String header) {
        String authHeaderValue = header.substring(BASIC_TYPE.length()).trim();
        byte[] decodedBytes = Base64.decodeBase64(authHeaderValue);
        String decodedString = new String(decodedBytes);
        return decodedString.split(DELIMITER);
    }
}

