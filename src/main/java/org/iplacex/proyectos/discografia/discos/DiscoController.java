package org.iplacex.proyectos.discografia.discos;

import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DiscoController {

    private final IDiscoRepository discosRepo;
    private final IArtistaRepository artistasRepo;

    public DiscoController(IDiscoRepository discosRepo, IArtistaRepository artistasRepo) {
        this.discosRepo = discosRepo;
        this.artistasRepo = artistasRepo;
    }

    // POST /disco
    @PostMapping(
        value = "/disco",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandlePostDiscoRequest(@RequestBody Disco body) {
        try {
            if (body.idArtista == null || body.idArtista.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("idArtista es requerido");
            }
            if (!artistasRepo.existsById(body.idArtista)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe artista con id=" + body.idArtista);
            }
            Disco saved = discosRepo.save(body);
            return ResponseEntity.created(URI.create("/api/disco/" + saved._id)).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear disco: " + ex.getMessage());
        }
    }

    // GET /discos
    @GetMapping(
        value = "/discos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Disco>> HandleGetDiscosRequest() {
        return ResponseEntity.ok(discosRepo.findAll());
    }

    // GET /disco/{id}
    @GetMapping(
        value = "/disco/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetDiscoRequest(@PathVariable("id") String id) {
        Optional<Disco> opt = discosRepo.findById(id);
        return opt.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Disco no encontrado para id=" + id));
    }

    // GET /artista/{id}/discos
    @GetMapping(
        value = "/artista/{id}/discos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Disco>> HandleGetDiscosByArtistaRequest(@PathVariable("id") String idArtista) {
        return ResponseEntity.ok(discosRepo.findDiscosByIdArtista(idArtista));
    }
}
