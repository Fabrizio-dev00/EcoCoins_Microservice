package com.ecocoins.ecocoins_microservice.config;

import com.ecocoins.ecocoins_microservice.model.PuntoRecoleccion;
import com.ecocoins.ecocoins_microservice.repository.PuntoRecoleccionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PuntoRecoleccionDataInitializer {

    private final PuntoRecoleccionRepository puntoRepository;

    public PuntoRecoleccionDataInitializer(PuntoRecoleccionRepository puntoRepository) {
        this.puntoRepository = puntoRepository;
    }

    @PostConstruct
    public void init() {
        if (puntoRepository.count() == 0) {
            System.out.println("⚙️ Inicializando puntos de recolección...");

            PuntoRecoleccion punto1 = new PuntoRecoleccion();
            punto1.setNombre("Punto Reciclaje Biblioteca");
            punto1.setDescripcion("Contenedores de papel, cartón y plástico");
            punto1.setUbicacion("Biblioteca Central, Piso 1");
            punto1.setLatitud(-12.046374);
            punto1.setLongitud(-77.042793);
            punto1.setTiposMaterialesAceptados(Arrays.asList("PAPEL", "CARTON", "PLASTICO"));
            punto1.setHorarioApertura("07:00");
            punto1.setHorarioCierre("22:00");
            punto1.setResponsable("María González");
            punto1.setTelefonoContacto("555-1001");
            punto1.setActivo(true);

            PuntoRecoleccion punto2 = new PuntoRecoleccion();
            punto2.setNombre("Punto Reciclaje Cafetería");
            punto2.setDescripcion("Contenedores para orgánicos y envases");
            punto2.setUbicacion("Cafetería Principal, Zona de mesas");
            punto2.setLatitud(-12.046500);
            punto2.setLongitud(-77.042900);
            punto2.setTiposMaterialesAceptados(Arrays.asList("ORGANICO", "PLASTICO", "VIDRIO"));
            punto2.setHorarioApertura("06:00");
            punto2.setHorarioCierre("20:00");
            punto2.setResponsable("Carlos Pérez");
            punto2.setTelefonoContacto("555-1002");
            punto2.setActivo(true);

            PuntoRecoleccion punto3 = new PuntoRecoleccion();
            punto3.setNombre("Punto Reciclaje Ingeniería");
            punto3.setDescripcion("Punto especializado en electrónicos");
            punto3.setUbicacion("Facultad de Ingeniería, Edificio A");
            punto3.setLatitud(-12.046200);
            punto3.setLongitud(-77.043100);
            punto3.setTiposMaterialesAceptados(Arrays.asList("ELECTRONICO", "METAL", "PLASTICO"));
            punto3.setHorarioApertura("08:00");
            punto3.setHorarioCierre("18:00");
            punto3.setResponsable("Ana Torres");
            punto3.setTelefonoContacto("555-1003");
            punto3.setActivo(true);

            PuntoRecoleccion punto4 = new PuntoRecoleccion();
            punto4.setNombre("Punto Reciclaje Estacionamiento");
            punto4.setDescripcion("Contenedores generales");
            punto4.setUbicacion("Estacionamiento Principal");
            punto4.setLatitud(-12.046600);
            punto4.setLongitud(-77.042600);
            punto4.setTiposMaterialesAceptados(Arrays.asList("PAPEL", "PLASTICO", "VIDRIO", "METAL"));
            punto4.setHorarioApertura("00:00");
            punto4.setHorarioCierre("23:59");
            punto4.setResponsable("Sistema Automatizado");
            punto4.setTelefonoContacto("555-1004");
            punto4.setActivo(true);

            PuntoRecoleccion punto5 = new PuntoRecoleccion();
            punto5.setNombre("Punto Reciclaje Aulas Verdes");
            punto5.setDescripcion("Zona ecológica multimaterial");
            punto5.setUbicacion("Edificio de Ciencias, Patio central");
            punto5.setLatitud(-12.046100);
            punto5.setLongitud(-77.042500);
            punto5.setTiposMaterialesAceptados(Arrays.asList("PAPEL", "CARTON", "PLASTICO", "VIDRIO", "ORGANICO"));
            punto5.setHorarioApertura("07:30");
            punto5.setHorarioCierre("19:00");
            punto5.setResponsable("Juan Martínez");
            punto5.setTelefonoContacto("555-1005");
            punto5.setActivo(true);

            puntoRepository.saveAll(Arrays.asList(punto1, punto2, punto3, punto4, punto5));
            System.out.println("✅ Puntos de recolección creados: " + puntoRepository.count());
        }
    }
}
