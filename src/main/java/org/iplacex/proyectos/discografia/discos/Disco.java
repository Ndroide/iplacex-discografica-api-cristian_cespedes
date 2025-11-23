package org.iplacex.proyectos.discografia.discos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("discos")
public class Disco {

    @Id
    public String _id;

    public String idArtista;

    public String nombre;

    public int anioLanzamiento;

    public List<String> canciones;
}
