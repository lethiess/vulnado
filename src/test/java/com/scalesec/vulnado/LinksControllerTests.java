package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private LinkLister linkLister;

    private MockMvc mockMvc;

    public LinksControllerTests() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void links_ShouldReturnLinks_WhenValidUrlProvided() throws Exception {
        // Arrange
        when(linkLister.getLinks(anyString())).thenReturn(Arrays.asList("http://example.com", "http://test.com"));

        // Act & Assert
        mockMvc.perform(get("/links")
                .param("url", "http://valid-url.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("http://example.com"))
                .andExpect(jsonPath("$[1]").value("http://test.com"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void links_ShouldReturnEmptyList_WhenNoLinksFound() throws Exception {
        // Arrange
        when(linkLister.getLinks(anyString())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/links")
                .param("url", "http://no-links.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void links_ShouldReturnError_WhenIOExceptionOccurs() throws Exception {
        // Arrange
        when(linkLister.getLinks(anyString())).thenThrow(new IOException("IO Exception occurred"));

        // Act & Assert
        mockMvc.perform(get("/links")
                .param("url", "http://error-url.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("IO Exception occurred"));
    }

    @Test
    public void linksV2_ShouldReturnLinks_WhenValidUrlProvided() throws Exception {
        // Arrange
        when(linkLister.getLinksV2(anyString())).thenReturn(Arrays.asList("http://example.com", "http://test.com"));

        // Act & Assert
        mockMvc.perform(get("/links-v2")
                .param("url", "http://valid-url.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("http://example.com"))
                .andExpect(jsonPath("$[1]").value("http://test.com"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void linksV2_ShouldReturnEmptyList_WhenNoLinksFound() throws Exception {
        // Arrange
        when(linkLister.getLinksV2(anyString())).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/links-v2")
                .param("url", "http://no-links.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void linksV2_ShouldReturnError_WhenBadRequestOccurs() throws Exception {
        // Arrange
        when(linkLister.getLinksV2(anyString())).thenThrow(new BadRequest("Bad Request occurred"));

        // Act & Assert
        mockMvc.perform(get("/links-v2")
                .param("url", "http://error-url.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Bad Request occurred"));
    }
}
