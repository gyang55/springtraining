package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblInventoryMedia;
import ca.emonster.training.ecommerce.repository.TblInventoryMediaRepository;
import ca.emonster.training.ecommerce.service.dto.TblInventoryMediaDTO;
import ca.emonster.training.ecommerce.service.mapper.TblInventoryMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblInventoryMedia}.
 */
@Service
@Transactional
public class TblInventoryMediaService {

    private final Logger log = LoggerFactory.getLogger(TblInventoryMediaService.class);

    private final TblInventoryMediaRepository tblInventoryMediaRepository;

    private final TblInventoryMediaMapper tblInventoryMediaMapper;

    public TblInventoryMediaService(
        TblInventoryMediaRepository tblInventoryMediaRepository,
        TblInventoryMediaMapper tblInventoryMediaMapper
    ) {
        this.tblInventoryMediaRepository = tblInventoryMediaRepository;
        this.tblInventoryMediaMapper = tblInventoryMediaMapper;
    }

    /**
     * Save a tblInventoryMedia.
     *
     * @param tblInventoryMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public TblInventoryMediaDTO save(TblInventoryMediaDTO tblInventoryMediaDTO) {
        log.debug("Request to save TblInventoryMedia : {}", tblInventoryMediaDTO);
        TblInventoryMedia tblInventoryMedia = tblInventoryMediaMapper.toEntity(tblInventoryMediaDTO);
        tblInventoryMedia = tblInventoryMediaRepository.save(tblInventoryMedia);
        return tblInventoryMediaMapper.toDto(tblInventoryMedia);
    }

    /**
     * Partially update a tblInventoryMedia.
     *
     * @param tblInventoryMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblInventoryMediaDTO> partialUpdate(TblInventoryMediaDTO tblInventoryMediaDTO) {
        log.debug("Request to partially update TblInventoryMedia : {}", tblInventoryMediaDTO);

        return tblInventoryMediaRepository
            .findById(tblInventoryMediaDTO.getId())
            .map(existingTblInventoryMedia -> {
                tblInventoryMediaMapper.partialUpdate(existingTblInventoryMedia, tblInventoryMediaDTO);

                return existingTblInventoryMedia;
            })
            .map(tblInventoryMediaRepository::save)
            .map(tblInventoryMediaMapper::toDto);
    }

    /**
     * Get all the tblInventoryMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblInventoryMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblInventoryMedias");
        return tblInventoryMediaRepository.findAll(pageable).map(tblInventoryMediaMapper::toDto);
    }

    /**
     * Get one tblInventoryMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblInventoryMediaDTO> findOne(Long id) {
        log.debug("Request to get TblInventoryMedia : {}", id);
        return tblInventoryMediaRepository.findById(id).map(tblInventoryMediaMapper::toDto);
    }

    /**
     * Delete the tblInventoryMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblInventoryMedia : {}", id);
        tblInventoryMediaRepository.deleteById(id);
    }
}
