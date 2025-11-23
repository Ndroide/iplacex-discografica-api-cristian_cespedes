package org.iplacex.proyectos.discografia.artistas;

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
public class ArtistaController {

    private final IArtistaRepository repo;

    public ArtistaController(IArtistaRepository repo) {
        this.repo = repo;
    }

    // POST /artista
    @PostMapping(
        value = "/artista",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleInsertArtistaRequest(@RequestBody Artista body) {
        try {
            Artista saved = repo.save(body);
            return ResponseEntity.created(URI.create("/api/artista/" + saved._id)).body(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear artista: " + ex.getMessage());
        }
    }

    // GET /artistas
    @GetMapping(
        value = "/artistas",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Artista>> HandleGetAristasRequest() {
        return ResponseEntity.ok(repo.findAll());
    }

    // GET /artista/{id}
    @GetMapping(
        value = "/artista/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetArtistaRequest(@PathVariable("id") String id) {
        Optional<Artista> opt = repo.findById(id);
        return opt.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artista no encontrado para id=" + id));
    }

    // UPDATE (PUT) /artista/{id}
    @PutMapping(
        value = "/artista/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleUpdateArtistaRequest(@PathVariable("id") String id,
                                                             @RequestBody Artista body) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe artista con id=" + id);
            }
            body._id = id;
            Artista updated = repo.save(body);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar artista: " + ex.getMessage());
        }
    }

    // DELETE /artista/{id}
    @DeleteMapping(
        value = "/artista/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleDeleteArtistaRequest(@PathVariable("id") String id) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existe artista con id=" + id);
            }
            repo.deleteById(id);
            return ResponseEntity.ok("Artista eliminado id=" + id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar artista: " + ex.getMessage());
        }
    }
}
