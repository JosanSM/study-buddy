package com.josan.study_buddy.Service;

import com.josan.study_buddy.Subject.Subject;
import com.josan.study_buddy.Subject.SubjectRepository;
import com.josan.study_buddy.Subject.SubjectService;
import com.josan.study_buddy.Subject.SubjectDto.AddSubjectRequest;
import com.josan.study_buddy.Subject.SubjectDto.GenericSubjectResponse;
import com.josan.study_buddy.Subject.SubjectDto.SubjectRequest;
import com.josan.study_buddy.Topic.Topic;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.User.UserService;
import com.josan.study_buddy.exception.DuplicateSubjectNameException;
import com.josan.study_buddy.exception.SubjectNotFoundException;
import com.josan.study_buddy.exception.SubjectNotEmptyException;
import com.josan.study_buddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTests {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubjectService subjectService;

    private User buildUser(long id) {
        return User.builder().id(id).name("User").email("user@example.com").user_tier("pro").build();
    }

    private Subject buildSubject(Long id, String name, User user, Set<Topic> topics) {
        return Subject.builder().id(id).name(name).user(user).topic(topics).build();
    }

    // findSubjectById

    @Test
    public void findSubjectById_SubjectExists_ReturnsGenericSubjectResponse() {
        Subject subject = buildSubject(1L, "Math", buildUser(1L), Collections.emptySet());
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        GenericSubjectResponse response = subjectService.findSubjectById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Math", response.getName());
    }

    @Test
    public void findSubjectById_SubjectNotFound_ThrowsSubjectNotFoundException() {
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class, () -> subjectService.findSubjectById(99L));
    }

    // addSubject

    @Test
    public void addSubject_ValidRequest_ReturnsGenericSubjectResponse() {
        User user = buildUser(1L);
        AddSubjectRequest request = AddSubjectRequest.builder().name("Math").userId(1L).build();
        Subject saved = buildSubject(1L, "Math", user, Collections.emptySet());

        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectRepository.existsByNameAndUserId("Math", 1L)).thenReturn(false);
        when(subjectRepository.save(any(Subject.class))).thenReturn(saved);

        GenericSubjectResponse response = subjectService.addSubject(request);

        assertNotNull(response);
        assertEquals("Math", response.getName());
    }

    @Test
    public void addSubject_UserNotFound_ThrowsUserNotFoundException() {
        AddSubjectRequest request = AddSubjectRequest.builder().name("Math").userId(99L).build();
        when(userService.findUserEntityById(99L)).thenThrow(new UserNotFoundException(99L));

        assertThrows(UserNotFoundException.class, () -> subjectService.addSubject(request));
        verify(subjectRepository, never()).save(any());
    }

    @Test
    public void addSubject_DuplicateName_ThrowsDuplicateSubjectNameException() {
        User user = buildUser(1L);
        AddSubjectRequest request = AddSubjectRequest.builder().name("Math").userId(1L).build();

        when(userService.findUserEntityById(1L)).thenReturn(user);
        when(subjectRepository.existsByNameAndUserId("Math", 1L)).thenReturn(true);

        assertThrows(DuplicateSubjectNameException.class, () -> subjectService.addSubject(request));
        verify(subjectRepository, never()).save(any());
    }

    // deleteSubjectById

    @Test
    public void deleteSubjectById_SubjectExistsNoTopics_DeletesSubject() {
        Subject subject = buildSubject(1L, "Math", buildUser(1L), Collections.emptySet());
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        subjectService.deleteSubjectById(1L);

        verify(subjectRepository).deleteById(1L);
    }

    @Test
    public void deleteSubjectById_SubjectNotFound_ThrowsSubjectNotFoundException() {
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class, () -> subjectService.deleteSubjectById(99L));
        verify(subjectRepository, never()).deleteById(any());
    }

    @Test
    public void deleteSubjectById_SubjectHasTopics_ThrowsSubjectNotEmptyException() {
        Subject subject = buildSubject(1L, "Math", buildUser(1L), Set.of(new Topic()));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        assertThrows(SubjectNotEmptyException.class, () -> subjectService.deleteSubjectById(1L));
        verify(subjectRepository, never()).deleteById(any());
    }

    // updateSubjectName

    @Test
    public void updateSubjectName_SubjectExists_ReturnsUpdatedResponse() {
        Subject subject = buildSubject(1L, "Math", buildUser(1L), Collections.emptySet());
        SubjectRequest request = SubjectRequest.builder().subjectId(1L).name("Physics").build();

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subject)).thenReturn(subject);

        GenericSubjectResponse response = subjectService.updateSubjectName(request);

        assertNotNull(response);
        verify(subjectRepository).save(subject);
    }

    @Test
    public void updateSubjectName_SubjectNotFound_ThrowsSubjectNotFoundException() {
        SubjectRequest request = SubjectRequest.builder().subjectId(99L).name("Physics").build();
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class, () -> subjectService.updateSubjectName(request));
    }
}
