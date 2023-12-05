package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblTaxExempt;
import ca.emonster.training.ecommerce.repository.TblTaxExemptRepository;
import ca.emonster.training.ecommerce.service.dto.TblTaxExemptDTO;
import ca.emonster.training.ecommerce.service.mapper.TblTaxExemptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblTaxExempt}.
 */
@Service
@Transactional
public class TblTaxExemptService {

    private final Logger log = LoggerFactory.getLogger(TblTaxExemptService.class);

    private final TblTaxExemptRepository tblTaxExemptRepository;

    private final TblTaxExemptMapper tblTaxExemptMapper;

    public TblTaxExemptService(TblTaxExemptRepository tblTaxExemptRepository, TblTaxExemptMapper tblTaxExemptMapper) {
        this.tblTaxExemptRepository = tblTaxExemptRepository;
        this.tblTaxExemptMapper = tblTaxExemptMapper;
    }

    /**
     * Save a tblTaxExempt.
     *
     * @param tblTaxExemptDTO the entity to save.
     * @return the persisted entity.
     */
    public TblTaxExemptDTO save(TblTaxExemptDTO tblTaxExemptDTO) {
        log.debug("Request to save TblTaxExempt : {}", tblTaxExemptDTO);
        TblTaxExempt tblTaxExempt = tblTaxExemptMapper.toEntity(tblTaxExemptDTO);
        tblTaxExempt = tblTaxExemptRepository.save(tblTaxExempt);
        return tblTaxExemptMapper.toDto(tblTaxExempt);
    }

    /**
     * Partially update a tblTaxExempt.
     *
     * @param tblTaxExemptDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblTaxExemptDTO> partialUpdate(TblTaxExemptDTO tblTaxExemptDTO) {
        log.debug("Request to partially update TblTaxExempt : {}", tblTaxExemptDTO);

        return tblTaxExemptRepository
            .findById(tblTaxExemptDTO.getId())
            .map(existingTblTaxExempt -> {
                tblTaxExemptMapper.partialUpdate(existingTblTaxExempt, tblTaxExemptDTO);

                return existingTblTaxExempt;
            })
            .map(tblTaxExemptRepository::save)
            .map(tblTaxExemptMapper::toDto);
    }

    /**
     * Get all the tblTaxExempts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblTaxExemptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblTaxExempts");
        return tblTaxExemptRepository.findAll(pageable).map(tblTaxExemptMapper::toDto);
    }

    /**
     * Get one tblTaxExempt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblTaxExemptDTO> findOne(Long id) {
        log.debug("Request to get TblTaxExempt : {}", id);
        return tblTaxExemptRepository.findById(id).map(tblTaxExemptMapper::toDto);
    }

    /**
     * Delete the tblTaxExempt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblTaxExempt : {}", id);
        tblTaxExemptRepository.deleteById(id);
    }
}
