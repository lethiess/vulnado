package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LinksControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private LinkLister linkLister;

    // Test for /links endpoint
    @Test
    public void links_ValidUrl_ShouldReturnLinks() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        Mockito.when(linkLister.getLinks(testUrl)).thenReturn(mockLinks);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity("/links?url=" + testUrl, List.class);

        // Assert
        Mockito.verify(linkLister).getLinks(testUrl);
        assert response.getStatusCode() == HttpStatus.OK : "Expected HTTP status 200 OK";
        assert response.getBody().size() == mockLinks.size() : "Expected response body size to match mock links size";
    }

    @Test
    public void links_InvalidUrl_ShouldReturnError() throws Exception {
        // Arrange
        String invalidUrl = "invalid-url";
        Mockito.when(linkLister.getLinks(invalidUrl)).thenThrow(new IOException("Invalid URL"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/links?url=" + invalidUrl, String.class);

        // Assert
        Mockito.verify(linkLister).getLinks(invalidUrl);
        assert response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR : "Expected HTTP status 500 Internal Server Error";
        assert response.getBody().contains("Invalid URL") : "Expected error message in response body";
    }

    // Test for /links-v2 endpoint
    @Test
    public void linksV2_ValidUrl_ShouldReturnLinks() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        Mockito.when(linkLister.getLinksV2(testUrl)).thenReturn(mockLinks);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity("/links-v2?url=" + testUrl, List.class);

        // Assert
        Mockito.verify(linkLister).getLinksV2(testUrl);
        assert response.getStatusCode() == HttpStatus.OK : "Expected HTTP status 200 OK";
        assert response.getBody().size() == mockLinks.size() : "Expected response body size to match mock links size";
    }

    @Test
    public void linksV2_InvalidUrl_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidUrl = "invalid-url";
        Mockito.when(linkLister.getLinksV2(invalidUrl)).thenThrow(new BadRequest("Invalid URL"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/links-v2?url=" + invalidUrl, String.class);

        // Assert
        Mockito.verify(linkLister).getLinksV2(invalidUrl);
        assert response.getStatusCode() == HttpStatus.BAD_REQUEST : "Expected HTTP status 400 Bad Request";
        assert response.getBody().contains("Invalid URL") : "Expected error message in response body";
    }
}
