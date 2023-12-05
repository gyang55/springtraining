package ca.emonster.training.ecommerce.service;

import ca.emonster.training.ecommerce.domain.TblProduct;
import ca.emonster.training.ecommerce.repository.TblProductRepository;
import ca.emonster.training.ecommerce.service.dto.TblProductDTO;
import ca.emonster.training.ecommerce.service.mapper.TblProductMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TblProduct}.
 */
@Service
@Transactional
public class TblProductService {

    private final Logger log = LoggerFactory.getLogger(TblProductService.class);

    private final TblProductRepository tblProductRepository;

    private final TblProductMapper tblProductMapper;

    public TblProductService(TblProductRepository tblProductRepository, TblProductMapper tblProductMapper) {
        this.tblProductRepository = tblProductRepository;
        this.tblProductMapper = tblProductMapper;
    }

    /**
     * Save a tblProduct.
     *
     * @param tblProductDTO the entity to save.
     * @return the persisted entity.
     */
    public TblProductDTO save(TblProductDTO tblProductDTO) {
        log.debug("Request to save TblProduct : {}", tblProductDTO);
        TblProduct tblProduct = tblProductMapper.toEntity(tblProductDTO);
        tblProduct = tblProductRepository.save(tblProduct);
        return tblProductMapper.toDto(tblProduct);
    }

    /**
     * Partially update a tblProduct.
     *
     * @param tblProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TblProductDTO> partialUpdate(TblProductDTO tblProductDTO) {
        log.debug("Request to partially update TblProduct : {}", tblProductDTO);

        return tblProductRepository
            .findById(tblProductDTO.getId())
            .map(existingTblProduct -> {
                tblProductMapper.partialUpdate(existingTblProduct, tblProductDTO);

                return existingTblProduct;
            })
            .map(tblProductRepository::save)
            .map(tblProductMapper::toDto);
    }

    /**
     * Get all the tblProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TblProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TblProducts");
        return tblProductRepository.findAll(pageable).map(tblProductMapper::toDto);
    }

    /**
     * Get one tblProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TblProductDTO> findOne(Long id) {
        log.debug("Request to get TblProduct : {}", id);
        return tblProductRepository.findById(id).map(tblProductMapper::toDto);
    }

    /**
     * Delete the tblProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TblProduct : {}", id);
        tblProductRepository.deleteById(id);
    }
}
