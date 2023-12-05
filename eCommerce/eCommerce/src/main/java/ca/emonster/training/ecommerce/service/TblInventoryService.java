package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblInventory;
import ca.emonster.training.ecommerce.repository.TblInventoryRepository;
import ca.emonster.training.ecommerce.service.dto.TblInventoryDTO;
import ca.emonster.training.ecommerce.service.mapper.TblInventoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblInventory}.
 */
@Service
@Transactional
public class TblInventoryService {

    private final Logger log = LoggerFactory.getLogger(TblInventoryService.class);

    private final TblInventoryRepository tblInventoryRepository;

    private final TblInventoryMapper tblInventoryMapper;

    public TblInventoryService(TblInventoryRepository tblInventoryRepository, TblInventoryMapper tblInventoryMapper) {
        this.tblInventoryRepository = tblInventoryRepository;
        this.tblInventoryMapper = tblInventoryMapper;
    }

    /**
     * Save a tblInventory.
     *
     * @param tblInventoryDTO the entity to save.
     * @return the persisted entity.
     */
    public TblInventoryDTO save(TblInventoryDTO tblInventoryDTO) {
        log.debug("Request to save TblInventory : {}", tblInventoryDTO);
        TblInventory tblInventory = tblInventoryMapper.toEntity(tblInventoryDTO);
        tblInventory = tblInventoryRepository.save(tblInventory);
        return tblInventoryMapper.toDto(tblInventory);
    }

    /**
     * Partially update a tblInventory.
     *
     * @param tblInventoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblInventoryDTO> partialUpdate(TblInventoryDTO tblInventoryDTO) {
        log.debug("Request to partially update TblInventory : {}", tblInventoryDTO);

        return tblInventoryRepository
            .findById(tblInventoryDTO.getId())
            .map(existingTblInventory -> {
                tblInventoryMapper.partialUpdate(existingTblInventory, tblInventoryDTO);

                return existingTblInventory;
            })
            .map(tblInventoryRepository::save)
            .map(tblInventoryMapper::toDto);
    }

    /**
     * Get all the tblInventories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblInventoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblInventories");
        return tblInventoryRepository.findAll(pageable).map(tblInventoryMapper::toDto);
    }

    /**
     * Get one tblInventory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblInventoryDTO> findOne(Long id) {
        log.debug("Request to get TblInventory : {}", id);
        return tblInventoryRepository.findById(id).map(tblInventoryMapper::toDto);
    }

    /**
     * Delete the tblInventory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblInventory : {}", id);
        tblInventoryRepository.deleteById(id);
    }
}
