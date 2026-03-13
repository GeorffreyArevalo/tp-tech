package com.pragma.bootcamps.tech.domain.usecases;

import com.pragma.bootcamps.tech.domain.exceptions.TechnologyAlreadyExistsException;
import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.domain.spi.TechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private TechnologyPersistencePort technologyPersistencePort;

    private TechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new TechnologyUseCase(technologyPersistencePort);
    }

    @Test
    void saveShouldPersistTechnologyWhenNameDoesNotExist() {
        Technology technology = Technology.builder()
                .id(1L)
                .name("Java")
                .description("Lenguaje orientado a objetos")
                .build();

        when(technologyPersistencePort.findByName("Java")).thenReturn(Mono.empty());
        when(technologyPersistencePort.save(technology)).thenReturn(Mono.just(technology));

        StepVerifier.create(useCase.save(technology))
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort).findByName("Java");
        verify(technologyPersistencePort).save(technology);
    }

    @Test
    void saveShouldFailWhenTechnologyAlreadyExists() {
        Technology input = Technology.builder().name("Java").description("Nueva").build();
        Technology existing = Technology.builder().id(99L).name("Java").description("Existente").build();

        when(technologyPersistencePort.findByName("Java")).thenReturn(Mono.just(existing));

        StepVerifier.create(useCase.save(input))
                .expectErrorSatisfies(error -> {
                    assertEquals(TechnologyAlreadyExistsException.class, error.getClass());
                    assertEquals("Technology with name Java already exists.", error.getMessage());
                })
                .verify();

        verify(technologyPersistencePort).findByName("Java");
        verify(technologyPersistencePort, never()).save(input);
    }

    @Test
    void saveShouldPropagateErrorFromFindByName() {
        Technology technology = Technology.builder().name("Java").description("Desc").build();
        RuntimeException expected = new RuntimeException("find failure");

        when(technologyPersistencePort.findByName("Java")).thenReturn(Mono.error(expected));

        StepVerifier.create(useCase.save(technology))
                .expectErrorMatches(error -> error == expected)
                .verify();

        verify(technologyPersistencePort).findByName("Java");
        verify(technologyPersistencePort, never()).save(technology);
    }

    @Test
    void saveShouldPropagateErrorFromSave() {
        Technology technology = Technology.builder().name("Java").description("Desc").build();
        RuntimeException expected = new RuntimeException("save failure");

        when(technologyPersistencePort.findByName("Java")).thenReturn(Mono.empty());
        when(technologyPersistencePort.save(technology)).thenReturn(Mono.error(expected));

        StepVerifier.create(useCase.save(technology))
                .expectErrorMatches(error -> error == expected)
                .verify();

        verify(technologyPersistencePort).findByName("Java");
        verify(technologyPersistencePort).save(technology);
    }

    @Test
    void saveShouldThrowNpeWhenTechnologyIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.save(null));

        verifyNoInteractions(technologyPersistencePort);
    }
}
