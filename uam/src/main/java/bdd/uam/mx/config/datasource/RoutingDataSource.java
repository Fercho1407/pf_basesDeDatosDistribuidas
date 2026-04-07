package bdd.uam.mx.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        Zona zona = ZonaContext.getZona();
        return zona != null ? zona : Zona.RURAL;
    }
}