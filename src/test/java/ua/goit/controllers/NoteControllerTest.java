package ua.goit.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.goit.note.*;
import ua.goit.users.UserDto;
import ua.goit.users.UserRole;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.data.repository.util.ClassUtils.hasProperty;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles({"test"})
public class NoteControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private NoteConverter noteConverter;

    @MockBean
    private NoteRepository noteRepository;



    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "VISITOR")
    void testGetNotesListPublicFormShouldReturnListPublicNotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notes/listPublic"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("listPublicNotes"));
    }

    @Test
    @WithMockUser(roles = "VISITOR")
    void testGetNotesCreateFormShouldReturnCreateNote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notes/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("createNote"));
    }

    @Test
    void testGetNotesCreateFormWithIncorrectUserShouldReturnHttpStatus302AndRedirectToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/notes/create"))
                .andExpect(MockMvcResultMatchers.status().is(302))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

//    @Test
//    @WithMockUser(roles = "VISITOR")
//    void testFindNoteByNameShouldReturnNote() throws Exception {
//        NoteDao note = noteConverter.toDao(prepareNote());
//        when(noteRepository.findByName("Test")).thenReturn(Optional.ofNullable(note));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/notes/find/name?name=Test"))
//                .andExpect(MockMvcResultMatchers.status().is(302))
//                .andExpect(MockMvcResultMatchers.view().name("findNote"));
//    }



    private NoteDto prepareNote() {
        return new NoteDto.NoteBuilder()
                .withId(UUID.randomUUID())
                .withName("Test")
                .withContent("Test access content")
                .withAccessType(Access.ACCESS_PRIVATE)
                .withUser(prepareUser())
                .build();
    }

    private UserDto prepareUser() {
        return new UserDto.UserBuilder()
                .withId(UUID.randomUUID())
                .withUsername("Test username")
                .withEmail("test@gmail.com")
                .withPassword("$2a$12$Ymy2NsY2mm72faISck74Uesp0nRRHwm/LkmFuCU7A8.DE56bjSu.K")
                .withUserRole(UserRole.ROLE_USER)
                .build();
    }
}

