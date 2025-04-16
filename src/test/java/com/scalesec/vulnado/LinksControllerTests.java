package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private LinkLister linkLister;

    // Helper method to initialize MockMvc
    private void setupMockMvc() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }
    }

    @Test
    public void links_ShouldReturnLinks_WhenValidUrlProvided() throws Exception {
        // Arrange
        setupMockMvc();
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(linkLister.getLinks(testUrl)).thenReturn(mockLinks);

        // Act & Assert
        mockMvc.perform(get("/links").param("url", testUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("http://example.com/link1"))
                .andExpect(jsonPath("$[1]").value("http://example.com/link2"));

        verify(linkLister, times(1)).getLinks(testUrl);
    }

    @Test
    public void links_ShouldReturnEmptyList_WhenNoLinksFound() throws Exception {
        // Arrange
        setupMockMvc();
        String testUrl = "http://example.com";
        when(linkLister.getLinks(testUrl)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/links").param("url", testUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(linkLister, times(1)).getLinks(testUrl);
    }

    @Test
    public void links_ShouldReturnServerError_WhenIOExceptionOccurs() throws Exception {
        // Arrange
        setupMockMvc();
        String testUrl = "http://example.com";
        when(linkLister.getLinks(testUrl)).thenThrow(new IOException("Test IOException"));

        // Act & Assert
        mockMvc.perform(get("/links").param("url", testUrl))
                .andExpect(status().isInternalServerError());

        verify(linkLister, times(1)).getLinks(testUrl);
    }

    @Test
    public void linksV2_ShouldReturnLinks_WhenValidUrlProvided() throws Exception {
        // Arrange
        setupMockMvc();
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(linkLister.getLinksV2(testUrl)).thenReturn(mockLinks);

        // Act & Assert
        mockMvc.perform(get("/links-v2").param("url", testUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("http://example.com/link1"))
                .andExpect(jsonPath("$[1]").value("http://example.com/link2"));

        verify(linkLister, times(1)).getLinksV2(testUrl);
    }

    @Test
    public void linksV2_ShouldReturnEmptyList_WhenNoLinksFound() throws Exception {
        // Arrange
        setupMockMvc();
        String testUrl = "http://example.com";
        when(linkLister.getLinksV2(testUrl)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/links-v2").param("url", testUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(linkLister, times(1)).getLinksV2(testUrl);
    }

    @Test
    public void linksV2_ShouldReturnBadRequest_WhenBadRequestExceptionOccurs() throws Exception {
        // Arrange
        setupMockMvc();
        String testUrl = "http://example.com";
        when(linkLister.getLinksV2(testUrl)).thenThrow(new BadRequest("Invalid URL"));

        // Act & Assert
        mockMvc.perform(get("/links-v2").param("url", testUrl))
                .andExpect(status().isBadRequest());

        verify(linkLister, times(1)).getLinksV2(testUrl);
    }
}
