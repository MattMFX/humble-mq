package br.edu.ufabc.mfmachado.humblemq.gateway.repository;

import br.edu.ufabc.mfmachado.humblemq.gateway.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {
    Optional<ChannelEntity> findByName(String name);
    Boolean existsByName(String name);
}
