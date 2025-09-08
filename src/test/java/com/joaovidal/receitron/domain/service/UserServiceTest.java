package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.adapter.in.web.dto.CategoryObject;
import com.joaovidal.receitron.adapter.in.web.dto.CategoryResponse;
import com.joaovidal.receitron.adapter.in.web.dto.CultureObject;
import com.joaovidal.receitron.adapter.in.web.dto.CultureResponse;
import com.joaovidal.receitron.adapter.out.api.MealdbApiAdapter;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryPort repositoryPort;

    @Mock
    private MealdbApiPort mealdbApiPort;

    @InjectMocks
    private UserService userService;

    String email = "tester@email.com";

    @Test
    void shouldFindUserByEmail() {
        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        var result = userService.findByEmail(email).get();

        assertEquals(result.getEmail(), email);
        verify(repositoryPort, times(1)).findByEmail(email);
    }

    @Test
    void shouldThrowUserNotFound() {
        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> {userService.findByEmail(email);});
        verify(repositoryPort, times(1)).findByEmail(email);
    }

    @Test
    void shouldFetchAndAddCulture() {
        var mockResponse = new CultureResponse(List.of(new CultureObject("Brazilian")));
        when(mealdbApiPort.listCultures()).thenReturn(mockResponse.meals().stream().map(CultureObject::culture).toList());

        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        var result = userService.addCultures(email, List.of("Brazilian"));

        assertEquals("Brazilian", result.getFavoriteCultures().stream().findFirst().get());
        verify(repositoryPort, times(1)).findByEmail(email);
        verify(mealdbApiPort, times(1)).listCultures();
    }

    @Test
    void shouldFetchAndAddPreferenceAndRestriction() {
        var mockResponse = new CategoryResponse(List.of(new CategoryObject("Seafood")));
        when(mealdbApiPort.listCategories()).thenReturn(mockResponse.meals().stream().map(CategoryObject::category).toList());

        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        var preference = userService.addPreferences(email, List.of("Seafood"));
        var restriction = userService.addRestrictions(email, List.of("Seafood"));

        assertEquals("Seafood", preference.getPreferences().stream().findFirst().get());
        assertEquals("Seafood", restriction.getPreferences().stream().findFirst().get());
        verify(repositoryPort, times(2)).findByEmail(email);
        verify(mealdbApiPort, times(2)).listCategories();
    }

    @Test
    void shouldThrowCultureNotFound() {
        var mockResponse = new CultureResponse(List.of(new CultureObject("Brazilian")));
        when(mealdbApiPort.listCultures()).thenReturn(mockResponse.meals().stream().map(CultureObject::culture).toList());

        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(ApiException.class, () -> {userService.addCultures(email, List.of("Canadian"));});

        verify(repositoryPort, times(1)).findByEmail(email);
        verify(mealdbApiPort, times(1)).listCultures();
    }

    @Test
    void shouldThrowCategoryNotFound() {
        var mockResponse = new CategoryResponse(List.of(new CategoryObject("Pork")));
        when(mealdbApiPort.listCategories()).thenReturn(mockResponse.meals().stream().map(CategoryObject::category).toList());

        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(ApiException.class, () -> {userService.addPreferences(email, List.of("Vegan"));});
        assertThrows(ApiException.class, () -> {userService.addRestrictions(email, List.of("Vegan"));});

        verify(repositoryPort, times(2)).findByEmail(email);
        verify(mealdbApiPort, times(2)).listCategories();
    }

}
