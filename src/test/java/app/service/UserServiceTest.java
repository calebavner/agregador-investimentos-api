package app.service;

import app.controller.dto.CreateUserDto;
import app.exceptions.AppException;
import app.exceptions.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static app.constants.UserConstants.CREATE_USER_VALID;
import static app.constants.UserConstants.USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> idArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Retorna o UUID do recurso criado")
        void ReturnUUID_WhenValidData() {

            doReturn(USER)
                    .when(repo)
                    .save(userArgumentCaptor.capture());

            var sut = service.createUser(CREATE_USER_VALID);
            var userCaptured = userArgumentCaptor.getValue();

            assertNotNull(sut);
            assertEquals(CREATE_USER_VALID.username(), userCaptured.getUsername());
            assertTrue(sut.compareTo(USER.getUserId()) == 0);
        }

        @Test
        @DisplayName("Deve lançar uma exceção quando o ID não for valido")
        void ThrowsException_WhenUserIsNotValid () {
            doThrow(new RuntimeException()).when(repo).save(any());
            assertThrows(RuntimeException.class, () -> service.createUser(CREATE_USER_VALID));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Retorna usuario quando UUID for valido")
        void ReturnUser_WhenIdIsValid() {

            doReturn(Optional.of(USER))
                    .when(repo)
                    .findById(idArgumentCaptor.capture());

            var sut = service.getUserById(USER.getUserId());

            assertEquals(USER.getUserId(), idArgumentCaptor.getValue());
            assertEquals(sut.getUserId(), USER.getUserId());
            assertEquals(sut, USER);
        }

        @Test
        @DisplayName("lança exceção quando ID não for valido")
        void ThrowsException_WhenIdIsNotValid() {

            when(repo.findById(USER.getUserId()))
                    .thenThrow(UserNotFoundException.class);


            assertThatThrownBy(() -> service
                    .getUserById(USER.getUserId()))
                    .isInstanceOf(RuntimeException.class);

            assertThatThrownBy(() -> service
                    .getUserById(USER.getUserId()))
                    .isInstanceOf(AppException.class);
        }
    }

    @Nested
    class getAllUsers {

        @Test
        @DisplayName("deve retornar lista de usuarios cadastrados")
        void ReturnListOfUsers() {

            doReturn(List.of(USER))
                    .when(repo)
                    .findAll();

            var sut = service.getAllUsers();

            assertFalse(sut.isEmpty());
            assertEquals(1, sut.size());
            assertEquals(sut.get(0), USER);
        }
    }

    @Nested
    class updateUser {

        @Test
        @DisplayName("Retorna o usuario cadastrados, se requisição for valida")
        void ReturnUserUpdated_WhenUserValid() {

            CreateUserDto dto = new CreateUserDto("Avner", "avner@email.com", "123");
            when(repo.findById(USER.getUserId()))
                    .thenReturn(Optional.of(USER));

            var sut = service.updateUser(USER.getUserId(), dto);
            assertEquals(sut, USER);
            assertEquals(sut.getUserId(), USER.getUserId());
            assertEquals(sut.getUsername(), dto.username());
        }
    }

    @Nested
    class deleteUser {

        @Test
        @DisplayName("Deve retornar sem conteudo, quando id for valido")
        void ReturnNoContent_WhenIdValid() {

            doReturn(true)
                    .when(repo)
                    .existsById(idArgumentCaptor.capture());

            doNothing()
                    .when(repo)
                    .deleteById(idArgumentCaptor.capture());

            service.deleteUser(USER.getUserId());
            var idList = idArgumentCaptor.getAllValues();

            assertEquals(USER.getUserId(), idArgumentCaptor.getValue());
            assertEquals(USER.getUserId(), idList.get(0));
            assertEquals(USER.getUserId(), idList.get(1));
        }
    }
}