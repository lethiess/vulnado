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
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private LinkLister linkLister;

    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void links_ShouldReturnLinks() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(linkLister.getLinks(testUrl)).thenReturn(mockLinks);

        // Act & Assert
        mockMvc.perform(get("/links").param("url", testUrl))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    assertTrue("Response should contain link1", responseContent.contains("http://example.com/link1"));
                    assertTrue("Response should contain link2", responseContent.contains("http://example.com/link2"));
                });
    }

    @Test
    public void links_ShouldHandleIOException() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        when(linkLister.getLinks(testUrl)).thenThrow(new IOException("Test IOException"));

        // Act & Assert
        mockMvc.perform(get("/links").param("url", testUrl))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    assertTrue("Response should contain error message", responseContent.contains("Test IOException"));
                });
    }

    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(linkLister.getLinksV2(testUrl)).thenReturn(mockLinks);

        // Act & Assert
        mockMvc.perform(get("/links-v2").param("url", testUrl))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    assertTrue("Response should contain link1", responseContent.contains("http://example.com/link1"));
                    assertTrue("Response should contain link2", responseContent.contains("http://example.com/link2"));
                });
    }

    @Test
    public void linksV2_ShouldHandleBadRequest() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        when(linkLister.getLinksV2(testUrl)).thenThrow(new BadRequest("Invalid URL"));

        // Act & Assert
        mockMvc.perform(get("/links-v2").param("url", testUrl))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    assertTrue("Response should contain error message", responseContent.contains("Invalid URL"));
                });
    }
}
