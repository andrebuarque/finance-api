package br.com.andrebuarque.financeapi.controller;

import br.com.andrebuarque.financeapi.dto.CategoryDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.service.CategoryService;
import br.com.andrebuarque.financeapi.stub.CategoryDtoStub;
import br.com.andrebuarque.financeapi.stub.CategoryStub;
import br.com.andrebuarque.financeapi.stub.UserStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    public static final String CATEGORIES_URL = "/categories";

    MockMvc mvc;

    @InjectMocks
    CategoryController controller;

    @Mock
    CategoryService service;

    @Captor
    ArgumentCaptor<CategoryDto> categoryDtoArgumentCaptor;

    User loggedUser = UserStub.getUser();
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
            .addInterceptors(new HandlerInterceptor() {
                @Override
                public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
                    request.setAttribute("loggedUser", loggedUser);
                    return true;
                }
            })
            .build();
    }

    @Test
    void testFindAll() throws Exception {
        final Category category = CategoryStub.getCategory();
        final User user = category.getUser();

        when(service.findAll(any(User.class))).thenReturn(Stream.of(category));

        mvc.perform(get(CATEGORIES_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(category.getId()))
            .andExpect(jsonPath("$[0].name").value(category.getName()))
            .andExpect(jsonPath("$[0].pattern").value(category.getPattern()))
            .andExpect(jsonPath("$[0].type").value(category.getType().toString()))
            .andExpect(jsonPath("$[0].user.id").value(user.getId()))
            .andExpect(jsonPath("$[0].user.name").value(user.getName()))
            .andExpect(jsonPath("$[0].user.username").value(user.getUsername()))
            .andExpect(jsonPath("$[0].user.lastname").value(user.getLastname()))
            .andExpect(jsonPath("$[0].user.email").value(user.getEmail()));
    }

    @Test
    void testCreateWithInvalidPayload() throws Exception {
        mvc.perform(post(CATEGORIES_URL)
            .content("{}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, times(0)).create(any(), any());
    }

    @Test
    void testCreate() throws Exception {
        final CategoryDto categoryDto = CategoryDtoStub.getCategory();
        final Category category = CategoryStub.getCategory();

        when(service.create(any(), any())).thenReturn(category);

        mvc.perform(post(CATEGORIES_URL)
            .content(objectMapper.writeValueAsString(categoryDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(category.getId()))
            .andExpect(jsonPath("$.name").value(category.getName()))
            .andExpect(jsonPath("$.pattern").value(category.getPattern()))
            .andExpect(jsonPath("$.type").value(category.getType().toString()));

        verify(service).create(any(User.class), categoryDtoArgumentCaptor.capture());

        final CategoryDto captorValue = categoryDtoArgumentCaptor.getValue();
        assertThat(captorValue.getName()).isEqualTo(categoryDto.getName());
        assertThat(captorValue.getPattern()).isEqualTo(categoryDto.getPattern());
        assertThat(captorValue.getType()).isEqualTo(categoryDto.getType());
    }

    @Test
    void testUpdateWithoutPayload() throws Exception {
        mvc.perform(put(CATEGORIES_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithInvalidPayload() throws Exception {
        mvc.perform(put(CATEGORIES_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithDifferentUser() throws Exception {
        final CategoryDto categoryDto = CategoryDtoStub.getCategory();

        doThrow(new InvalidUserException()).when(service).update(any(), any(), any());

        mvc.perform(put(CATEGORIES_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto)))
            .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateWithUnknownCategory() throws Exception {
        final CategoryDto categoryDto = CategoryDtoStub.getCategory();

        doThrow(new CategoryNotFoundException()).when(service).update(any(), any(), any());

        mvc.perform(put(CATEGORIES_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate() throws Exception {
        final CategoryDto categoryDto = CategoryDtoStub.getCategory();
        final Category category = CategoryStub.getCategory();

        when(service.update(any(), any(), any())).thenReturn(category);

        mvc.perform(put(CATEGORIES_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(category.getId()))
            .andExpect(jsonPath("$.name").value(category.getName()))
            .andExpect(jsonPath("$.pattern").value(category.getPattern()))
            .andExpect(jsonPath("$.type").value(category.getType().toString()));
    }

    @Test
    void testFindById() throws Exception {
        final Category category = CategoryStub.getCategory();

        when(service.findById(any(), any())).thenReturn(category);

        mvc.perform(get(CATEGORIES_URL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(category.getId()))
            .andExpect(jsonPath("$.name").value(category.getName()))
            .andExpect(jsonPath("$.pattern").value(category.getPattern()))
            .andExpect(jsonPath("$.type").value(category.getType().toString()));
    }

    @Test
    void testFindByIdWithDifferentUser() throws Exception {
        doThrow(new InvalidUserException()).when(service).findById(any(), any());

        mvc.perform(get(CATEGORIES_URL + "/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    void testFindByIdWithUnknownCategoryId() throws Exception {
        doThrow(new CategoryNotFoundException()).when(service).findById(any(), any());

        mvc.perform(get(CATEGORIES_URL + "/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        mvc.perform(delete(CATEGORIES_URL + "/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteByIdWithDifferentUser() throws Exception {
        doThrow(new InvalidUserException()).when(service).deleteById(any(), any());

        mvc.perform(delete(CATEGORIES_URL + "/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteByIdWithUnknownCategoryId() throws Exception {
        doThrow(new CategoryNotFoundException()).when(service).deleteById(any(), any());

        mvc.perform(delete(CATEGORIES_URL + "/1"))
            .andExpect(status().isNotFound());
    }
}