package com.maple.cse308.repository;

import com.maple.cse308.entity.Episode;
import com.maple.cse308.entity.EpisodeIdentity;
import com.maple.cse308.entity.SeasonIdentity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends CrudRepository<Episode, EpisodeIdentity> {

    Episode save(Episode episode);

    List<EpisodeIdentity> findAllByEpisodeIdSeasonId(SeasonIdentity seasonIdentity);

    Episode findByEpisodeId(EpisodeIdentity episodeId);
}
