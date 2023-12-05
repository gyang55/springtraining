package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblItem;
import ca.emonster.training.ecommerce.repository.TblItemRepository;
import ca.emonster.training.ecommerce.service.dto.TblItemDTO;
import ca.emonster.training.ecommerce.service.mapper.TblItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblItem}.
 */
@Service
@Transactional
public class TblItemService {

    private final Logger log = LoggerFactory.getLogger(TblItemService.class);

    private final TblItemRepository tblItemRepository;

    private final TblItemMapper tblItemMapper;

    public TblItemService(TblItemRepository tblItemRepository, TblItemMapper tblItemMapper) {
        this.tblItemRepository = tblItemRepository;
        this.tblItemMapper = tblItemMapper;
    }

    /**
     * Save a tblItem.
     *
     * @param tblItemDTO the entity to save.
     * @return the persisted entity.
     */
    public TblItemDTO save(TblItemDTO tblItemDTO) {
        log.debug("Request to save TblItem : {}", tblItemDTO);
        TblItem tblItem = tblItemMapper.toEntity(tblItemDTO);
        tblItem = tblItemRepository.save(tblItem);
        return tblItemMapper.toDto(tblItem);
    }

    /**
     * Partially update a tblItem.
     *
     * @param tblItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblItemDTO> partialUpdate(TblItemDTO tblItemDTO) {
        log.debug("Request to partially update TblItem : {}", tblItemDTO);

        return tblItemRepository
            .findById(tblItemDTO.getId())
            .map(existingTblItem -> {
                tblItemMapper.partialUpdate(existingTblItem, tblItemDTO);

                return existingTblItem;
            })
            .map(tblItemRepository::save)
            .map(tblItemMapper::toDto);
    }

    /**
     * Get all the tblItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblItems");
        return tblItemRepository.findAll(pageable).map(tblItemMapper::toDto);
    }

    /**
     * Get one tblItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblItemDTO> findOne(Long id) {
        log.debug("Request to get TblItem : {}", id);
        return tblItemRepository.findById(id).map(tblItemMapper::toDto);
    }

    /**
     * Delete the tblItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblItem : {}", id);
        tblItemRepository.deleteById(id);
    }
}
