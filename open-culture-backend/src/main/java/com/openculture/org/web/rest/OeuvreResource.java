package com.openculture.org.web.rest;

import com.openculture.org.domain.TypeOeuvre;
import com.openculture.org.domain.enumeration.TypeFichier;
import com.openculture.org.repository.TypeOeuvreRepository;
import com.openculture.org.service.OeuvreService;
import com.openculture.org.service.dto.FiltreDTO;
import com.openculture.org.service.dto.OeuvreDTO;
import com.openculture.org.service.dto.TypeOeuvreDTO;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link com.openculture.org.domain.Oeuvre}.
 */
@RestController
@RequestMapping("/api")
public class OeuvreResource {

    private final Logger log = LoggerFactory.getLogger(OeuvreResource.class);

    private static final String ENTITY_NAME = "oeuvre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OeuvreService oeuvreService;

    private final TypeOeuvreRepository typeOeuvreRepository;

    public OeuvreResource(OeuvreService oeuvreService,TypeOeuvreRepository typeOeuvreRepository) {
        this.oeuvreService = oeuvreService;
        this.typeOeuvreRepository = typeOeuvreRepository;
    }

    /**
     * {@code POST  /oeuvres} : Create a new oeuvre.
     *
     * @param oeuvreDTO the oeuvreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oeuvreDTO, or with status {@code 400 (Bad Request)} if the oeuvre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oeuvres")
    public ResponseEntity<OeuvreDTO> createOeuvre(@RequestBody OeuvreDTO oeuvreDTO) throws Exception {
        log.debug("REST request to save Oeuvre : {}", oeuvreDTO);
        if (oeuvreDTO.getId() != null) {
            throw new BadRequestAlertException("A new oeuvre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OeuvreDTO result = oeuvreService.save(oeuvreDTO);
        return ResponseEntity.created(new URI("/api/oeuvres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oeuvres} : Updates an existing oeuvre.
     *
     * @param oeuvreDTO the oeuvreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oeuvreDTO,
     * or with status {@code 400 (Bad Request)} if the oeuvreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oeuvreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oeuvres")
    public ResponseEntity<OeuvreDTO> updateOeuvre(@RequestBody OeuvreDTO oeuvreDTO) throws Exception {
        log.debug("REST request to update Oeuvre : {}", oeuvreDTO);
        if (oeuvreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OeuvreDTO result = oeuvreService.save(oeuvreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oeuvreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /oeuvres} : get all the oeuvres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oeuvres in body.
     */
    @GetMapping("/oeuvres/filter/{typeFichier}")
    public ResponseEntity<List<OeuvreDTO>> getAllOeuvres(@PathVariable TypeFichier typeFichier,Pageable pageable) {
        log.debug("REST request to get a page of Oeuvres");
        Page<OeuvreDTO> page = oeuvreService.findAllByTypeFichier(typeFichier,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/oeuvres")
    public ResponseEntity<List<OeuvreDTO>> getAllOeuvres(Pageable pageable) {
        log.debug("REST request to get a page of Oeuvres");
        Page<OeuvreDTO> page = oeuvreService.findComplet(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/oeuvres-for-gestionnaire")
    public ResponseEntity<List<OeuvreDTO>> findAllOeuvres(Pageable pageable) {
        log.debug("REST request to get a page of Oeuvres");
        Page<OeuvreDTO> page = oeuvreService.findCompletForAdmin(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /oeuvres/:id} : get the "id" oeuvre.
     *
     * @param id the id of the oeuvreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oeuvreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oeuvres/{id}")
    public ResponseEntity<OeuvreDTO> getOeuvre(@PathVariable Long id) {
        log.debug("REST request to get Oeuvre : {}", id);
        OeuvreDTO oeuvreDTO = oeuvreService.findOne(id);
        return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oeuvreDTO.getId().toString()))
        .body(oeuvreDTO);
    }

    /**
     * {@code DELETE  /oeuvres/:id} : delete the "id" oeuvre.
     *
     * @param id the id of the oeuvreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oeuvres/{id}")
    public ResponseEntity<Void> deleteOeuvre(@PathVariable Long id) {
        log.debug("REST request to delete Oeuvre : {}", id);
        oeuvreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @CrossOrigin("http://localhost:8080")
    @GetMapping("/test/{id}")
    public ResponseEntity<Object> getVideo(@PathVariable Long id) {
        return oeuvreService.readMedia(id);
    }

    @GetMapping("oeuvres/my-recent-post-oeuvres")
    public ResponseEntity<List<OeuvreDTO>> getAllMyRecentPostsOeuvres(@RequestParam("categorie") String categorie, Pageable pageable) {
        log.debug("REST request to get a page of Oeuvres");

        // @RequestBody FiltreDTO filtreDTO,Pageable pageable
        // FiltreDTO filtreDTO = new FiltreDTO();
        // filtreDTO.setId((long) 1);
        // filtreDTO.setString("admin");
        // Pageable pageable;

        Page<OeuvreDTO> page = oeuvreService.findRecentsPostsOeuvreByUser(categorie,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("oeuvres/recent-post-oeuvres")
    public ResponseEntity<List<OeuvreDTO>> getAllRecentPostsOeuvres(@RequestParam("categorie") String categorie, Pageable pageable) {
        log.debug("REST request to get a page of Oeuvres");

        // @RequestBody FiltreDTO filtreDTO,Pageable pageable
        // FiltreDTO filtreDTO = new FiltreDTO();
        // filtreDTO.setId((long) 1);
        // Pageable pageable;
        Page<OeuvreDTO> page;
            
        if(typeOeuvreRepository.findAll().stream().map(TypeOeuvre::getIntitule).collect(Collectors.toList()).contains(categorie)){
            page = oeuvreService.findRecentsPostsOeuvre(categorie,pageable);
        } else {
            page = oeuvreService.findCompletForAdmin(pageable);
        }
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


}
