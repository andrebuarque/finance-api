package br.com.andrebuarque.financeapi.controller;

import br.com.andrebuarque.financeapi.dto.TransactionDto;
import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.Transaction;
import br.com.andrebuarque.financeapi.entity.User;
import br.com.andrebuarque.financeapi.exception.CategoryNotFoundException;
import br.com.andrebuarque.financeapi.exception.InvalidUserException;
import br.com.andrebuarque.financeapi.exception.TransactionNotFoundException;
import br.com.andrebuarque.financeapi.service.TransactionService;
import br.com.andrebuarque.financeapi.stub.TransactionStub;
import br.com.andrebuarque.financeapi.stub.TransactionStubDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    public static final String TRANSACTIONS_URL = "/transactions";

    MockMvc mvc;

    @InjectMocks
    TransactionController controller;

    @Mock
    TransactionService service;

    @Captor
    ArgumentCaptor<TransactionDto> transactionDtoArgumentCaptor;

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
        final Transaction transaction = TransactionStub.getTransaction();
        final User user = transaction.getUser();
        final Category category = transaction.getCategory();

        when(service.findAll(any(User.class))).thenReturn(Stream.of(transaction));

        mvc.perform(get(TRANSACTIONS_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(transaction.getId()))
            .andExpect(jsonPath("$[0].date").value(transaction.getDate().toString()))
            .andExpect(jsonPath("$[0].value").value(transaction.getValue()))
            .andExpect(jsonPath("$[0].description").value(transaction.getDescription()))
            .andExpect(jsonPath("$[0].status").value(transaction.getStatus().toString()))
            .andExpect(jsonPath("$[0].type").value(transaction.getType().toString()))
            .andExpect(jsonPath("$[0].category.id").value(category.getId()))
            .andExpect(jsonPath("$[0].category.pattern").value(category.getPattern()))
            .andExpect(jsonPath("$[0].category.type").value(category.getType().toString()))
            .andExpect(jsonPath("$[0].category.name").value(category.getName()))
            .andExpect(jsonPath("$[0].user.id").value(user.getId()))
            .andExpect(jsonPath("$[0].user.name").value(user.getName()))
            .andExpect(jsonPath("$[0].user.username").value(user.getUsername()))
            .andExpect(jsonPath("$[0].user.lastname").value(user.getLastname()))
            .andExpect(jsonPath("$[0].user.email").value(user.getEmail()));
    }

    @Test
    void testCreateWithInvalidPayload() throws Exception {
        mvc.perform(post(TRANSACTIONS_URL)
            .content("{}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        verify(service, times(0)).create(any(), any());
    }

    @Test
    void testCreateWithUnknownCategory() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();
        doThrow(new CategoryNotFoundException()).when(service).create(any(), any());

        mvc.perform(post(TRANSACTIONS_URL)
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateWithInvalidUserFromCategory() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();
        doThrow(new InvalidUserException()).when(service).create(any(), any());

        mvc.perform(post(TRANSACTIONS_URL)
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void testCreate() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();
        final Transaction transaction = TransactionStub.getTransaction();

        when(service.create(any(), any())).thenReturn(transaction);

        mvc.perform(post(TRANSACTIONS_URL)
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(transaction.getId()))
            .andExpect(jsonPath("$.value").value(transaction.getValue()))
            .andExpect(jsonPath("$.date").value(transaction.getDate().toString()))
            .andExpect(jsonPath("$.status").value(transaction.getStatus().toString()))
            .andExpect(jsonPath("$.type").value(transaction.getType().toString()))
            .andExpect(jsonPath("$.description").value(transaction.getDescription()))
            .andExpect(jsonPath("$.user").isNotEmpty())
            .andExpect(jsonPath("$.category").isNotEmpty());
    }

    @Test
    void testUpdateWithUnknownTransaction() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();
        doThrow(new TransactionNotFoundException()).when(service).update(any(), any(), any());

        mvc.perform(put(TRANSACTIONS_URL + "/1")
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWithUnknownCategory() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();
        doThrow(new CategoryNotFoundException()).when(service).update(any(), any(), any());

        mvc.perform(put(TRANSACTIONS_URL + "/1")
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWithInvalidUser() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();
        doThrow(new InvalidUserException()).when(service).update(any(), any(), any());

        mvc.perform(put(TRANSACTIONS_URL + "/1")
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void testUpdate() throws Exception {
        final TransactionDto transactionDto = TransactionStubDto.getTransactionDto();

        mvc.perform(put(TRANSACTIONS_URL + "/1")
            .content(objectMapper.writeValueAsString(transactionDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(service).update(eq(loggedUser), eq("1"), any(TransactionDto.class));
    }

    @Test
    void testFindByIdWithInvalidUser() throws Exception {
        doThrow(new InvalidUserException()).when(service).findById(any(), any());

        mvc.perform(get(TRANSACTIONS_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void testFindByIdWithUnknownTransaction() throws Exception {
        doThrow(new TransactionNotFoundException()).when(service).findById(any(), any());

        mvc.perform(get(TRANSACTIONS_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testFindById() throws Exception {
        final Transaction transaction = TransactionStub.getTransaction();

        when(service.findById(any(), any())).thenReturn(transaction);

        mvc.perform(get(TRANSACTIONS_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(transaction.getId()))
            .andExpect(jsonPath("$.value").value(transaction.getValue()))
            .andExpect(jsonPath("$.date").value(transaction.getDate().toString()))
            .andExpect(jsonPath("$.status").value(transaction.getStatus().toString()))
            .andExpect(jsonPath("$.type").value(transaction.getType().toString()))
            .andExpect(jsonPath("$.description").value(transaction.getDescription()))
            .andExpect(jsonPath("$.user").isNotEmpty())
            .andExpect(jsonPath("$.category").isNotEmpty());
    }

    @Test
    void testDeleteByIdWithInvalidUser() throws Exception {
        doThrow(new InvalidUserException()).when(service).deleteById(any(), any());

        mvc.perform(delete(TRANSACTIONS_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteByIdWithUnknownTransaction() throws Exception {
        doThrow(new TransactionNotFoundException()).when(service).deleteById(any(), any());

        mvc.perform(delete(TRANSACTIONS_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById() throws Exception {
        mvc.perform(delete(TRANSACTIONS_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(service).deleteById(eq(loggedUser), eq("1"));
    }
}