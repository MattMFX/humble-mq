package br.edu.ufabc.mfmachado.humblemq.gateway.repository;

import br.edu.ufabc.mfmachado.humblemq.gateway.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {

    void deleteByName(String name);
}
