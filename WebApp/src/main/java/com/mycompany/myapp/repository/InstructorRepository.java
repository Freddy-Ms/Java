package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Instructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Instructor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstructorRepository extends ReactiveCrudRepository<Instructor, Long>, InstructorRepositoryInternal {
    Flux<Instructor> findAllBy(Pageable pageable);

    @Override
    <S extends Instructor> Mono<S> save(S entity);

    @Override
    Flux<Instructor> findAll();

    @Override
    Mono<Instructor> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InstructorRepositoryInternal {
    <S extends Instructor> Mono<S> save(S entity);

    Flux<Instructor> findAllBy(Pageable pageable);

    Flux<Instructor> findAll();

    Mono<Instructor> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Instructor> findAllBy(Pageable pageable, Criteria criteria);
}
