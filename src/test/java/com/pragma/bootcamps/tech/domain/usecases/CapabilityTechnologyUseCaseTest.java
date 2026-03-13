package com.pragma.bootcamps.tech.domain.usecases;

import com.pragma.bootcamps.tech.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.tech.domain.exceptions.InvalidCountException;
import com.pragma.bootcamps.tech.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.tech.domain.exceptions.RepeatedTechnologiesException;
import com.pragma.bootcamps.tech.domain.models.Technology;
import com.pragma.bootcamps.tech.domain.spi.CapabilityTechnologyPersistencePort;
import com.pragma.bootcamps.tech.domain.spi.TechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.pragma.bootcamps.tech.domain.constants.CapabilityConstants.MAX_TECHS;
import static com.pragma.bootcamps.tech.domain.constants.CapabilityConstants.MIN_TECHS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CapabilityTechnologyUseCaseTest {

	private static final Long CAPABILITY_ID = 1L;

	@Mock
	private CapabilityTechnologyPersistencePort capabilityTechnologyPersistencePort;

	@Mock
	private TechnologyPersistencePort technologyPersistencePort;

	@InjectMocks
	private CapabilityTechnologyUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new CapabilityTechnologyUseCase(capabilityTechnologyPersistencePort, technologyPersistencePort);
	}

	@Test
	void associateTechnologiesShouldCompleteWhenDataIsValid() {
		List<Long> ids = List.of(10L, 11L, 12L);

		when(technologyPersistencePort.countByIds(ids)).thenReturn(Mono.just(3L));
		when(capabilityTechnologyPersistencePort.saveAll(CAPABILITY_ID, ids)).thenReturn(Mono.empty());

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.verifyComplete();

		verify(technologyPersistencePort).countByIds(ids);
		verify(capabilityTechnologyPersistencePort).saveAll(CAPABILITY_ID, ids);
	}

	@Test
	void associateTechnologiesShouldFailWhenListHasLessThanMinimum() {
		List<Long> ids = List.of(10L, 11L);

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.expectErrorSatisfies(error -> {
					assertEquals(InvalidCountException.class, error.getClass());
					assertEquals(ExceptionMessages.INVALID_COUNT.format(MIN_TECHS, MAX_TECHS), error.getMessage());
				})
				.verify();

		verify(technologyPersistencePort, never()).countByIds(ids);
		verify(capabilityTechnologyPersistencePort, never()).saveAll(CAPABILITY_ID, ids);
	}

	@Test
	void associateTechnologiesShouldFailWhenListHasMoreThanMaximum() {
		List<Long> ids = java.util.stream.LongStream.rangeClosed(1, 21).boxed().toList();

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.expectErrorSatisfies(error -> {
					assertEquals(InvalidCountException.class, error.getClass());
					assertEquals(ExceptionMessages.INVALID_COUNT.format(MIN_TECHS, MAX_TECHS), error.getMessage());
				})
				.verify();

		verify(technologyPersistencePort, never()).countByIds(ids);
		verify(capabilityTechnologyPersistencePort, never()).saveAll(CAPABILITY_ID, ids);
	}

	@Test
	void associateTechnologiesShouldFailWhenListHasDuplicatedIds() {
		List<Long> ids = List.of(10L, 10L, 12L);

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.expectErrorSatisfies(error -> {
					assertEquals(RepeatedTechnologiesException.class, error.getClass());
					assertEquals(ExceptionMessages.REPEATED_TECHS.format(), error.getMessage());
				})
				.verify();

		verify(technologyPersistencePort, never()).countByIds(ids);
		verify(capabilityTechnologyPersistencePort, never()).saveAll(CAPABILITY_ID, ids);
	}

	@Test
	void associateTechnologiesShouldFailWhenSomeTechnologyIdDoesNotExist() {
		List<Long> ids = List.of(10L, 11L, 12L);

		when(technologyPersistencePort.countByIds(ids)).thenReturn(Mono.just(2L));

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.expectErrorSatisfies(error -> {
					assertEquals(NotFoundException.class, error.getClass());
					assertEquals(ExceptionMessages.TECH_NOT_FOUND.format(), error.getMessage());
				})
				.verify();

		verify(capabilityTechnologyPersistencePort, never()).saveAll(CAPABILITY_ID, ids);
	}

	@Test
	void associateTechnologiesShouldPropagateErrorFromCountByIds() {
		List<Long> ids = List.of(10L, 11L, 12L);
		RuntimeException expected = new RuntimeException("count failure");

		when(technologyPersistencePort.countByIds(ids)).thenReturn(Mono.error(expected));

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.expectErrorMatches(error -> error == expected)
				.verify();

		verify(capabilityTechnologyPersistencePort, never()).saveAll(CAPABILITY_ID, ids);
	}

	@Test
	void associateTechnologiesShouldPropagateErrorFromSaveAll() {
		List<Long> ids = List.of(10L, 11L, 12L);
		RuntimeException expected = new RuntimeException("save failure");

		when(technologyPersistencePort.countByIds(ids)).thenReturn(Mono.just(3L));
		when(capabilityTechnologyPersistencePort.saveAll(CAPABILITY_ID, ids)).thenReturn(Mono.error(expected));

		StepVerifier.create(useCase.associateTechnologies(CAPABILITY_ID, ids))
				.expectErrorMatches(error -> error == expected)
				.verify();
	}

	@Test
	void associateTechnologiesShouldThrowNpeWhenIdsAreNull() {
		assertThrows(NullPointerException.class, () -> useCase.associateTechnologies(CAPABILITY_ID, null));

		verifyNoInteractions(technologyPersistencePort);
		verifyNoInteractions(capabilityTechnologyPersistencePort);
	}

	@Test
	void getTechnologiesByCapabilityIdShouldReturnTechnologiesForEachAssociation() {
		Technology java = Technology.builder().id(10L).name("Java").description("Java desc").build();
		Technology kotlin = Technology.builder().id(11L).name("Kotlin").description("Kotlin desc").build();

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
				.thenReturn(Flux.just(10L, 11L));
		when(technologyPersistencePort.findTechnologyById(10L)).thenReturn(Mono.just(java));
		when(technologyPersistencePort.findTechnologyById(11L)).thenReturn(Mono.just(kotlin));

		StepVerifier.create(useCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
				.expectNext(java)
				.expectNext(kotlin)
				.verifyComplete();

		verify(technologyPersistencePort).findTechnologyById(10L);
		verify(technologyPersistencePort).findTechnologyById(11L);
	}

	@Test
	void getTechnologiesByCapabilityIdShouldSkipMissingTechnologyRecords() {
		Technology java = Technology.builder().id(10L).name("Java").description("Java desc").build();

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
				.thenReturn(Flux.just(10L, 11L));
		when(technologyPersistencePort.findTechnologyById(10L)).thenReturn(Mono.just(java));
		when(technologyPersistencePort.findTechnologyById(11L)).thenReturn(Mono.empty());

		StepVerifier.create(useCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
				.expectNext(java)
				.verifyComplete();
	}

	@Test
	void getTechnologiesByCapabilityIdShouldPropagateErrorFromCapabilityLookup() {
		RuntimeException expected = new RuntimeException("lookup failure");

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
				.thenReturn(Flux.error(expected));

		StepVerifier.create(useCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
				.expectErrorMatches(error -> error == expected)
				.verify();
	}

	@Test
	void getTechnologiesByCapabilityIdShouldPropagateErrorFromTechnologyLookup() {
		RuntimeException expected = new RuntimeException("technology lookup failure");

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityId(CAPABILITY_ID))
				.thenReturn(Flux.just(10L));
		when(technologyPersistencePort.findTechnologyById(10L)).thenReturn(Mono.error(expected));

		StepVerifier.create(useCase.getTechnologiesByCapabilityId(CAPABILITY_ID))
				.expectErrorMatches(error -> error == expected)
				.verify();
	}

	@Test
	void deleteTechnologiesByCapabilityIdsShouldDeleteOnlyAssociationsWhenNoTechnologiesFound() {
		List<Long> capabilityIds = List.of(1L, 2L);

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityIds(capabilityIds))
				.thenReturn(Flux.empty());
		when(capabilityTechnologyPersistencePort.deleteAssociationsByCapabilityIds(capabilityIds))
				.thenReturn(Mono.empty());

		StepVerifier.create(useCase.deleteTechnologiesByCapabilityIds(capabilityIds))
				.verifyComplete();

		verify(capabilityTechnologyPersistencePort).deleteAssociationsByCapabilityIds(capabilityIds);
		verify(capabilityTechnologyPersistencePort, never()).countOtherCapacityAssociations(org.mockito.ArgumentMatchers.anyLong(), org.mockito.ArgumentMatchers.anyList());
		verify(technologyPersistencePort, never()).deleteTechnologiesByIds(org.mockito.ArgumentMatchers.anyList());
	}

	@Test
	void deleteTechnologiesByCapabilityIdsShouldDeleteOnlyDistinctOrphansAndThenAssociations() {
		List<Long> capabilityIds = List.of(1L, 2L);

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityIds(capabilityIds))
				.thenReturn(Flux.just(10L, 10L, 20L, 30L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(10L, capabilityIds)).thenReturn(Mono.just(0L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(20L, capabilityIds)).thenReturn(Mono.just(2L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(30L, capabilityIds)).thenReturn(Mono.just(0L));
		when(technologyPersistencePort.deleteTechnologiesByIds(List.of(10L, 30L))).thenReturn(Mono.empty());
		when(capabilityTechnologyPersistencePort.deleteAssociationsByCapabilityIds(capabilityIds)).thenReturn(Mono.empty());

		StepVerifier.create(useCase.deleteTechnologiesByCapabilityIds(capabilityIds))
				.verifyComplete();

		verify(capabilityTechnologyPersistencePort).countOtherCapacityAssociations(10L, capabilityIds);
		verify(capabilityTechnologyPersistencePort).countOtherCapacityAssociations(20L, capabilityIds);
		verify(capabilityTechnologyPersistencePort).countOtherCapacityAssociations(30L, capabilityIds);
		verify(capabilityTechnologyPersistencePort, times(1)).countOtherCapacityAssociations(10L, capabilityIds);
		verify(technologyPersistencePort).deleteTechnologiesByIds(List.of(10L, 30L));
		verify(capabilityTechnologyPersistencePort).deleteAssociationsByCapabilityIds(capabilityIds);
	}

	@Test
	void deleteTechnologiesByCapabilityIdsShouldSkipTechnologyDeletionWhenNoOrphans() {
		List<Long> capabilityIds = List.of(1L, 2L);

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityIds(capabilityIds))
				.thenReturn(Flux.just(10L, 20L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(10L, capabilityIds)).thenReturn(Mono.just(1L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(20L, capabilityIds)).thenReturn(Mono.just(3L));
		when(capabilityTechnologyPersistencePort.deleteAssociationsByCapabilityIds(capabilityIds)).thenReturn(Mono.empty());

		StepVerifier.create(useCase.deleteTechnologiesByCapabilityIds(capabilityIds))
				.verifyComplete();

		verify(technologyPersistencePort, never()).deleteTechnologiesByIds(org.mockito.ArgumentMatchers.anyList());
		verify(capabilityTechnologyPersistencePort).deleteAssociationsByCapabilityIds(capabilityIds);
	}

	@Test
	void deleteTechnologiesByCapabilityIdsShouldPropagateErrorFromAssociationCount() {
		List<Long> capabilityIds = List.of(1L, 2L);
		RuntimeException expected = new RuntimeException("count associations failure");

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityIds(capabilityIds))
				.thenReturn(Flux.just(10L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(10L, capabilityIds))
				.thenReturn(Mono.error(expected));
		when(capabilityTechnologyPersistencePort.deleteAssociationsByCapabilityIds(capabilityIds))
				.thenReturn(Mono.empty());

		StepVerifier.create(useCase.deleteTechnologiesByCapabilityIds(capabilityIds))
				.expectErrorMatches(error -> error == expected)
				.verify();

		verify(capabilityTechnologyPersistencePort, times(1)).deleteAssociationsByCapabilityIds(capabilityIds);
	}

	@Test
	void deleteTechnologiesByCapabilityIdsShouldPropagateErrorFromOrphanDeletion() {
		List<Long> capabilityIds = List.of(1L, 2L);
		RuntimeException expected = new RuntimeException("delete technologies failure");

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityIds(capabilityIds))
				.thenReturn(Flux.just(10L));
		when(capabilityTechnologyPersistencePort.countOtherCapacityAssociations(10L, capabilityIds)).thenReturn(Mono.just(0L));
		when(technologyPersistencePort.deleteTechnologiesByIds(List.of(10L))).thenReturn(Mono.error(expected));
		when(capabilityTechnologyPersistencePort.deleteAssociationsByCapabilityIds(capabilityIds))
				.thenReturn(Mono.empty());

		StepVerifier.create(useCase.deleteTechnologiesByCapabilityIds(capabilityIds))
				.expectErrorMatches(error -> error == expected)
				.verify();

		verify(capabilityTechnologyPersistencePort, times(1)).deleteAssociationsByCapabilityIds(capabilityIds);
	}

	@Test
	void deleteTechnologiesByCapabilityIdsShouldPropagateErrorFromFindTechnologyIds() {
		List<Long> capabilityIds = List.of(1L, 2L);
		RuntimeException expected = new RuntimeException("find ids failure");

		when(capabilityTechnologyPersistencePort.findTechnologyIdsByCapabilityIds(capabilityIds))
				.thenReturn(Flux.error(expected));
		when(capabilityTechnologyPersistencePort.deleteAssociationsByCapabilityIds(capabilityIds))
				.thenReturn(Mono.empty());

		StepVerifier.create(useCase.deleteTechnologiesByCapabilityIds(capabilityIds))
				.expectErrorMatches(error -> error == expected)
				.verify();

		verify(capabilityTechnologyPersistencePort, times(1)).deleteAssociationsByCapabilityIds(capabilityIds);
	}
}
