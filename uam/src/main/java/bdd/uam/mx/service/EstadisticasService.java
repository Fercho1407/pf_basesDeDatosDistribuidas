package bdd.uam.mx.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.config.datasource.Zona;
import bdd.uam.mx.config.datasource.ZonaContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticasService {
    private final EstadisticasTransactionalService estadisticasTransactionalService;
    
    public List<ConteoPersonasEntidadDTO> getNumPersonasByEntidadGlobal() {
        // ConcurrentHashMap porque varios hilos van a escribir al mismo tiempo
        Map<String, Long> acumuladorGlobal = new ConcurrentHashMap<>();

        // lista de tareas (hilos) asíncronas
        List<CompletableFuture<Void>> tareas = Stream.of(Zona.values())
            .map(zona -> CompletableFuture.runAsync(() -> {
                try {
                    // El contexto se setea DENTRO del nuevo hilo
                    ZonaContext.setZona(zona);
                    
                    List<ConteoPersonasEntidadDTO> resultadosParciales =
                        estadisticasTransactionalService.getNumPersonasByEntidad();

                    for (ConteoPersonasEntidadDTO parcial : resultadosParciales) {
                        acumuladorGlobal.merge(
                            parcial.getNombre(),
                            parcial.getTotalPersonas(),
                            Long::sum
                        );
                    }
                } finally {
                    ZonaContext.clear();
                }
            }))
            .toList();

        // Esperar a que los 3 nodos terminen de responder
        CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0])).join();

        return acumuladorGlobal.entrySet().stream()
                .map(entry -> new ConteoPersonasEntidadDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}