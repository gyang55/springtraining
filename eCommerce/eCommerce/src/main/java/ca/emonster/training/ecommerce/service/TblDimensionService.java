package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblDimension;
import ca.emonster.training.ecommerce.repository.TblDimensionRepository;
import ca.emonster.training.ecommerce.service.dto.TblDimensionDTO;
import ca.emonster.training.ecommerce.service.mapper.TblDimensionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblDimension}.
 */
@Service
@Transactional
public class TblDimensionService {

    private final Logger log = LoggerFactory.getLogger(TblDimensionService.class);

    private final TblDimensionRepository tblDimensionRepository;

    private final TblDimensionMapper tblDimensionMapper;

    public TblDimensionService(TblDimensionRepository tblDimensionRepository, TblDimensionMapper tblDimensionMapper) {
        this.tblDimensionRepository = tblDimensionRepository;
        this.tblDimensionMapper = tblDimensionMapper;
    }

    /**
     * Save a tblDimension.
     *
     * @param tblDimensionDTO the entity to save.
     * @return the persisted entity.
     */
    public TblDimensionDTO save(TblDimensionDTO tblDimensionDTO) {
        log.debug("Request to save TblDimension : {}", tblDimensionDTO);
        TblDimension tblDimension = tblDimensionMapper.toEntity(tblDimensionDTO);
        tblDimension = tblDimensionRepository.save(tblDimension);
        return tblDimensionMapper.toDto(tblDimension);
    }

    /**
     * Partially update a tblDimension.
     *
     * @param tblDimensionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblDimensionDTO> partialUpdate(TblDimensionDTO tblDimensionDTO) {
        log.debug("Request to partially update TblDimension : {}", tblDimensionDTO);

        return tblDimensionRepository
            .findById(tblDimensionDTO.getId())
            .map(existingTblDimension -> {
                tblDimensionMapper.partialUpdate(existingTblDimension, tblDimensionDTO);

                return existingTblDimension;
            })
            .map(tblDimensionRepository::save)
            .map(tblDimensionMapper::toDto);
    }

    /**
     * Get all the tblDimensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblDimensionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblDimensions");
        return tblDimensionRepository.findAll(pageable).map(tblDimensionMapper::toDto);
    }

    /**
     *  Get all the tblDimensions where Inventory is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TblDimensionDTO> findAllWhereInventoryIsNull() {
        log.debug("Request to get all tblDimensions where Inventory is null");
        return StreamSupport
            .stream(tblDimensionRepository.findAll().spliterator(), false)
            .filter(tblDimension -> tblDimension.getInventory() == null)
            .map(tblDimensionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tblDimension by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblDimensionDTO> findOne(Long id) {
        log.debug("Request to get TblDimension : {}", id);
        return tblDimensionRepository.findById(id).map(tblDimensionMapper::toDto);
    }

    /**
     * Delete the tblDimension by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblDimension : {}", id);
        tblDimensionRepository.deleteById(id);
    }
}
