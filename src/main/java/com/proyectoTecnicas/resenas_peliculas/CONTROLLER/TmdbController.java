package com.proyectoTecnicas.resenas_peliculas.CONTROLLER;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class TmdbController {
        @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.base.url}")
    private String baseUrl;

    // Buscar películas en TMDB
    @GetMapping("/tmdb/buscar")
    public Object buscar(@RequestParam String query) {
        String url = baseUrl + "/search/movie?api_key=" + apiKey
                   + "&query=" + query
                   + "&language=es-MX";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Object.class);
    }

    // Servir imágenes de TMDB a través de tu servidor
    @GetMapping("/imagen")
    public void obtenerImagen(@RequestParam String url, HttpServletResponse response) throws Exception {
        response.sendRedirect(url);
    }
    @GetMapping("/tmdb/detalles/{id}")
public Object obtenerDetalles(@PathVariable int id) {
    String urlDetalles = baseUrl + "/movie/" + id + "?api_key=" + apiKey + "&language=es-MX";
    String urlCredits = baseUrl + "/movie/" + id + "/credits?api_key=" + apiKey;
    
    RestTemplate restTemplate = new RestTemplate();
    
    Map<String, Object> detalles = restTemplate.getForObject(urlDetalles, Map.class);
    Map<String, Object> credits = restTemplate.getForObject(urlCredits, Map.class);
    
    // Extraer director de los credits
    List<Map<String, Object>> crew = (List<Map<String, Object>>) credits.get("crew");
    String director = crew.stream()
        .filter(p -> "Director".equals(p.get("job")))
        .map(p -> (String) p.get("name"))
        .findFirst()
        .orElse("");
    
    // Extraer géneros del detalle
    List<Map<String, Object>> generos = (List<Map<String, Object>>) detalles.get("genres");
    String genero = generos.isEmpty() ? "" : (String) generos.get(0).get("name");
    
    detalles.put("directorNombre", director);
    detalles.put("generoNombre", genero);
    
    return detalles;
}
 
}
