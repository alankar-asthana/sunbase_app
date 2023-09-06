package com.example.sunbase_app.service;

        import com.fasterxml.jackson.databind.JsonNode;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpMethod;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${authentication.api.url}")
    private String authApiUrl;
    private String bearerToken;

    public String authenticateUser(String loginId, String password) {
        // Set up the request body and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"login_id\":\"" + loginId + "\",\"password\":\"" + password + "\"}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Send a POST request to the authentication API
        ResponseEntity<String> response = new RestTemplate().exchange(
                authApiUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        // Check if the request was successful (status code 200)
        if (response.getStatusCode().is2xxSuccessful()) {

            String responseBody = response.getBody();
            String token = extractTokenFromResponse(responseBody);
            return token;
        } else {
            return null;
        }
    }

    // Implement a method to extract the token from the API response
    private String extractTokenFromResponse(String responseBody) {
        try {
            // Create an instance of the Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON response
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Extract the "access_token" from the JSON response
            String accessToken = jsonNode.get("access_token").asText();
            this.bearerToken = accessToken;
            return accessToken;
        } catch (Exception e) {
            // Handle JSON parsing or other exceptions here
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }

    public String getBearerToken() {
        return this.bearerToken;
    }
}


