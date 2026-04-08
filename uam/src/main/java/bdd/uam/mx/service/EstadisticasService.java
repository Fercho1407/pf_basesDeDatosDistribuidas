package bdd.uam.mx.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.DTO.EscolaridadPorEntidadDTO;
import bdd.uam.mx.DTO.HogaresPorMunicipioDTO;
import bdd.uam.mx.DTO.SueldoPromedioEntidadDTO;
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



    public List<SueldoPromedioEntidadDTO> getSalariosPromedioEntidadGlobal() {
   
    Map<String, double[]> acumuladorGlobal = new ConcurrentHashMap<>();

    // Lista de tareas (hilos) asíncronas
    List<CompletableFuture<Void>> tareas = Stream.of(Zona.values())
        .map(zona -> CompletableFuture.runAsync(() -> {
            try {
                // El contexto se setea DENTRO del nuevo hilo
                ZonaContext.setZona(zona);
                
                List<SueldoPromedioEntidadDTO> resultadosParciales =
                    estadisticasTransactionalService.getSalariosPromedioEntidad();

                for (SueldoPromedioEntidadDTO parcial : resultadosParciales) {
                    // Protecciones contra nulos por si algún dato viene vacío de BD
                    double promedioNodo = parcial.getSalarioPromedio() != null ? parcial.getSalarioPromedio() : 0.0;
                    long conteoNodo = parcial.getTotalPersona() != null ? parcial.getTotalPersona() : 0L;

                    // 1. Calculamos cuánto dinero en total representa este nodo
                    double dineroTotalNodo = promedioNodo * conteoNodo;

                    // 2. Acumulamos de forma atómica y segura (Thread-Safe)
                    acumuladorGlobal.compute(parcial.getNombreEntidad(), (key, valoresActuales) -> {
                        if (valoresActuales == null) {
                            valoresActuales = new double[]{0.0, 0.0};
                        }
                        valoresActuales[0] += dineroTotalNodo; // Sumamos el dinero
                        valoresActuales[1] += conteoNodo;      // Sumamos las personas
                        return valoresActuales;
                    });
                }
            } finally {
                ZonaContext.clear();
            }
        }))
        .toList();

    // Esperar a que los 3 nodos terminen de responder
    CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0])).join();

    // 3 y 4. Calculamos el promedio ponderado final y convertimos a DTO
    return acumuladorGlobal.entrySet().stream()
        .map(entry -> {
            String nombreEntidad = entry.getKey();
            double sumaDineroGlobal = entry.getValue()[0];
            long totalPersonasGlobal = (long) entry.getValue()[1];

            // Evitamos división por cero en caso de que no haya personas
            double promedioFinal = totalPersonasGlobal > 0 ? (sumaDineroGlobal / totalPersonasGlobal) : 0.0;
            
            // Redondeamos a 2 decimales para moneda
            double promedioRedondeado = Math.round(promedioFinal * 100.0) / 100.0;

            return new SueldoPromedioEntidadDTO(nombreEntidad, promedioRedondeado, totalPersonasGlobal);
        })
        .toList();
    }


    public List<HogaresPorMunicipioDTO> getNumHogaresByMunicipioGlobal() {
        // Usamos ConcurrentHashMap porque varios hilos escribirán al mismo tiempo.
        Map<String, Long> acumuladorGlobal = new ConcurrentHashMap<>();

        // Creamos una lista de tareas (hilos) asíncronas, una por cada Zona (nodo)
        List<CompletableFuture<Void>> tareas = Stream.of(Zona.values())
            .map(zona -> CompletableFuture.runAsync(() -> {
                try {
                    // 1. Seteamos el contexto DENTRO del hilo para que consulte el nodo correcto
                    ZonaContext.setZona(zona);
                    
                    // 2. Traemos los datos de ese nodo en particular
                    List<HogaresPorMunicipioDTO> resultadosParciales =
                        estadisticasTransactionalService.getNumHogaresByMunicipio();

                    // 3. Sumamos los hogares al acumulador global
                    for (HogaresPorMunicipioDTO parcial : resultadosParciales) {
                        
                        // Protección contra nulos por seguridad
                        long hogaresNodo = parcial.getNumHogares() != null ? parcial.getNumHogares() : 0L;
                        
                        acumuladorGlobal.merge(
                            parcial.getNombreMunicipio(),
                            hogaresNodo,
                            Long::sum // Si el municipio ya existe, suma el valor nuevo al viejo
                        );
                    }
                } finally {
                    // 4. Limpiamos el hilo para evitar fugas de contexto
                    ZonaContext.clear();
                }
            }))
            .toList();

        // Esperamos a que los 3 nodos (URBANA, SUBURBANA, RURAL) terminen de responder
        CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0])).join();

        // Convertimos el mapa final de vuelta a la lista de DTOs esperada
        return acumuladorGlobal.entrySet().stream()
                .map(entry -> new HogaresPorMunicipioDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<EscolaridadPorEntidadDTO> getEscolaridadByEntidadGlobal(String abreviatura) {
        // Usamos ConcurrentHashMap para acumular de forma segura entre múltiples hilos.
        // Llave: Descripción de la escolaridad | Valor: Suma total de personas
        Map<String, Long> acumuladorGlobal = new ConcurrentHashMap<>();

        // Creamos los hilos asíncronos para consultar cada nodo (RURAL, URBANA, SUBURBANA)
        List<CompletableFuture<Void>> tareas = Stream.of(Zona.values())
            .map(zona -> CompletableFuture.runAsync(() -> {
                try {
                    // Seteamos el contexto DENTRO del hilo
                    ZonaContext.setZona(zona);
                    
                    // Ejecutamos la consulta pasando la abreviatura del estado
                    List<EscolaridadPorEntidadDTO> resultadosParciales =
                        estadisticasTransactionalService.getEscolaridadByEntidad(abreviatura);

                    // Consolidamos los resultados
                    for (EscolaridadPorEntidadDTO parcial : resultadosParciales) {
                        
                        // Protección contra nulos
                        long personasNodo = parcial.getTotalPersonas() != null ? parcial.getTotalPersonas() : 0L;
                        
                        acumuladorGlobal.merge(
                            parcial.getDescripcionEscolaridad(),
                            personasNodo,
                            Long::sum // Suma los valores atómicamente
                        );
                    }
                } finally {
                    // Limpiamos el contexto para liberar el hilo
                    ZonaContext.clear();
                }
            }))
            .toList();

        // Esperamos a que todos los nodos terminen su consulta
        CompletableFuture.allOf(tareas.toArray(new CompletableFuture[0])).join();

        // Convertimos el mapa final a la lista de DTOs esperada
        return acumuladorGlobal.entrySet().stream()
                .map(entry -> new EscolaridadPorEntidadDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}