package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Helper method to mock Jsoup Document and Elements
    private Document mockJsoupDocumentWithLinks(List<String> links) {
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        List<Element> mockElementList = new ArrayList<>();

        for (String link : links) {
            Element mockElement = mock(Element.class);
            when(mockElement.absUrl("href")).thenReturn(link);
            mockElementList.add(mockElement);
        }

        when(mockElements.iterator()).thenReturn(mockElementList.iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);

        return mockDocument;
    }

    @Test
    public void getLinks_ShouldReturnAllLinks() throws IOException {
        // Arrange
        List<String> expectedLinks = List.of("http://example.com", "http://test.com");
        Document mockDocument = mockJsoupDocumentWithLinks(expectedLinks);
        Mockito.mockStatic(Jsoup.class);
        when(Jsoup.connect("http://mockurl.com").get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinks("http://mockurl.com");

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);

        // Cleanup
        Mockito.clearAllCaches();
    }

    @Test(expected = IOException.class)
    public void getLinks_ShouldThrowIOException_WhenJsoupFails() throws IOException {
        // Arrange
        Mockito.mockStatic(Jsoup.class);
        when(Jsoup.connect("http://mockurl.com").get()).thenThrow(new IOException("Connection failed"));

        // Act
        LinkLister.getLinks("http://mockurl.com");

        // Cleanup
        Mockito.clearAllCaches();
    }

    @Test
    public void getLinksV2_ShouldReturnAllLinks_WhenUrlIsValid() throws BadRequest, IOException {
        // Arrange
        List<String> expectedLinks = List.of("http://example.com", "http://test.com");
        Document mockDocument = mockJsoupDocumentWithLinks(expectedLinks);
        Mockito.mockStatic(Jsoup.class);
        when(Jsoup.connect("http://mockurl.com").get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinksV2("http://mockurl.com");

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);

        // Cleanup
        Mockito.clearAllCaches();
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenUrlIsPrivateIP() throws BadRequest {
        // Act
        LinkLister.getLinksV2("http://192.168.0.1");
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenUrlIsInvalid() throws BadRequest {
        // Act
        LinkLister.getLinksV2("invalid-url");
    }

    @Test
    public void getLinksV2_ShouldThrowBadRequest_WithCorrectMessage_WhenExceptionOccurs() {
        // Arrange
        String invalidUrl = "invalid-url";
        String expectedMessage = "no protocol: invalid-url";

        try {
            // Act
            LinkLister.getLinksV2(invalidUrl);
            fail("Expected BadRequest exception was not thrown");
        } catch (BadRequest e) {
            // Assert
            assertEquals("The exception message should match the expected message", expectedMessage, e.getMessage());
        }
    }
}
