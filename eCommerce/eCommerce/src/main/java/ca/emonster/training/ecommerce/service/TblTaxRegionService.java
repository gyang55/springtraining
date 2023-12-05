package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblTaxRegion;
import ca.emonster.training.ecommerce.repository.TblTaxRegionRepository;
import ca.emonster.training.ecommerce.service.dto.TblTaxRegionDTO;
import ca.emonster.training.ecommerce.service.mapper.TblTaxRegionMapper;
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
 * Service Implementation for managing {@link TblTaxRegion}.
 */
@Service
@Transactional
public class TblTaxRegionService {

    private final Logger log = LoggerFactory.getLogger(TblTaxRegionService.class);

    private final TblTaxRegionRepository tblTaxRegionRepository;

    private final TblTaxRegionMapper tblTaxRegionMapper;

    public TblTaxRegionService(TblTaxRegionRepository tblTaxRegionRepository, TblTaxRegionMapper tblTaxRegionMapper) {
        this.tblTaxRegionRepository = tblTaxRegionRepository;
        this.tblTaxRegionMapper = tblTaxRegionMapper;
    }

    /**
     * Save a tblTaxRegion.
     *
     * @param tblTaxRegionDTO the entity to save.
     * @return the persisted entity.
     */
    public TblTaxRegionDTO save(TblTaxRegionDTO tblTaxRegionDTO) {
        log.debug("Request to save TblTaxRegion : {}", tblTaxRegionDTO);
        TblTaxRegion tblTaxRegion = tblTaxRegionMapper.toEntity(tblTaxRegionDTO);
        tblTaxRegion = tblTaxRegionRepository.save(tblTaxRegion);
        return tblTaxRegionMapper.toDto(tblTaxRegion);
    }

    /**
     * Partially update a tblTaxRegion.
     *
     * @param tblTaxRegionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblTaxRegionDTO> partialUpdate(TblTaxRegionDTO tblTaxRegionDTO) {
        log.debug("Request to partially update TblTaxRegion : {}", tblTaxRegionDTO);

        return tblTaxRegionRepository
            .findById(tblTaxRegionDTO.getId())
            .map(existingTblTaxRegion -> {
                tblTaxRegionMapper.partialUpdate(existingTblTaxRegion, tblTaxRegionDTO);

                return existingTblTaxRegion;
            })
            .map(tblTaxRegionRepository::save)
            .map(tblTaxRegionMapper::toDto);
    }

    /**
     * Get all the tblTaxRegions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblTaxRegionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblTaxRegions");
        return tblTaxRegionRepository.findAll(pageable).map(tblTaxRegionMapper::toDto);
    }

    /**
     *  Get all the tblTaxRegions where TaxExempt is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TblTaxRegionDTO> findAllWhereTaxExemptIsNull() {
        log.debug("Request to get all tblTaxRegions where TaxExempt is null");
        return StreamSupport
            .stream(tblTaxRegionRepository.findAll().spliterator(), false)
            .filter(tblTaxRegion -> tblTaxRegion.getTaxExempt() == null)
            .map(tblTaxRegionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tblTaxRegion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblTaxRegionDTO> findOne(Long id) {
        log.debug("Request to get TblTaxRegion : {}", id);
        return tblTaxRegionRepository.findById(id).map(tblTaxRegionMapper::toDto);
    }

    /**
     * Delete the tblTaxRegion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblTaxRegion : {}", id);
        tblTaxRegionRepository.deleteById(id);
    }
}
