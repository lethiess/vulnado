package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Test for getLinks method
    @Test
    public void getLinks_ValidUrl_ShouldReturnLinks() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        String mockHtml = "<html><body><a href=\"http://example.com/link1\">Link1</a><a href=\"http://example.com/link2\">Link2</a></body></html>";
        Document mockDocument = Jsoup.parse(mockHtml);
        Jsoup mockJsoup = mock(Jsoup.class);
        when(mockJsoup.connect(testUrl).get()).thenReturn(mockDocument);

        // Act
        List<String> links = LinkLister.getLinks(testUrl);

        // Assert
        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 2", 2, links.size());
        assertTrue("Links should contain http://example.com/link1", links.contains("http://example.com/link1"));
        assertTrue("Links should contain http://example.com/link2", links.contains("http://example.com/link2"));
    }

    @Test(expected = IOException.class)
    public void getLinks_InvalidUrl_ShouldThrowIOException() throws IOException {
        // Arrange
        String invalidUrl = "http://invalid-url.com";

        // Act
        LinkLister.getLinks(invalidUrl);

        // Assert
        // Exception is expected
    }

    // Test for getLinksV2 method
    @Test
    public void getLinksV2_ValidUrl_ShouldReturnLinks() throws BadRequest {
        // Arrange
        String testUrl = "http://example.com";
        String mockHtml = "<html><body><a href=\"http://example.com/link1\">Link1</a><a href=\"http://example.com/link2\">Link2</a></body></html>";
        Document mockDocument = Jsoup.parse(mockHtml);
        Jsoup mockJsoup = mock(Jsoup.class);
        when(mockJsoup.connect(testUrl).get()).thenReturn(mockDocument);

        // Act
        List<String> links = LinkLister.getLinksV2(testUrl);

        // Assert
        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 2", 2, links.size());
        assertTrue("Links should contain http://example.com/link1", links.contains("http://example.com/link1"));
        assertTrue("Links should contain http://example.com/link2", links.contains("http://example.com/link2"));
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_PrivateIp_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        String privateIpUrl = "http://192.168.1.1";

        // Act
        LinkLister.getLinksV2(privateIpUrl);

        // Assert
        // Exception is expected
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_InvalidUrl_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        String invalidUrl = "http://invalid-url.com";

        // Act
        LinkLister.getLinksV2(invalidUrl);

        // Assert
        // Exception is expected
    }
}
