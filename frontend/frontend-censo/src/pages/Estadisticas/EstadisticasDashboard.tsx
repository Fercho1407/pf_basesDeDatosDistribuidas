// src/pages/Estadisticas/EstadisticasDashboard.tsx
import React, { useState, useEffect } from 'react';
import { 
  ConteoPersonasEntidadDTO, 
  SueldoPromedioEntidadDTO, 
  HogaresPorMunicipioDTO, 
  EscolaridadPorEntidadDTO,
  EntidadFederativa
} from '../../types';
import { EstadisticaService } from '../../services/estadistica.api';
import { GeografiaService } from '../../services/geografia.api';
import './EstadisticasDashboard.css';

type Tab = 'HABITANTES' | 'SALARIOS' | 'HOGARES' | 'ESCOLARIDAD';

const EstadisticasDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState<Tab>('HABITANTES');
  
  // Estados para almacenar datos
  const [datosHabitantes, setDatosHabitantes] = useState<ConteoPersonasEntidadDTO[]>([]);
  const [datosSalarios, setDatosSalarios] = useState<SueldoPromedioEntidadDTO[]>([]);
  const [datosHogares, setDatosHogares] = useState<HogaresPorMunicipioDTO[]>([]);
  const [datosEscolaridad, setDatosEscolaridad] = useState<EscolaridadPorEntidadDTO[]>([]);
  
  // Estados para el filtro de escolaridad
  const [entidades, setEntidades] = useState<EntidadFederativa[]>([]);
  const [entidadSeleccionada, setEntidadSeleccionada] = useState<string>('');

  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar lista de entidades (solo se ejecuta una vez) para el filtro de Escolaridad
  useEffect(() => {
    GeografiaService.getEntidadesFederativas()
      .then(setEntidades)
      .catch(err => console.error("Error cargando entidades", err));
  }, []);

  // Efecto principal: Cargar datos según la pestaña activa
  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      setError(null);
      try {
        if (activeTab === 'HABITANTES' && datosHabitantes.length === 0) {
          const res = await EstadisticaService.getNumPersonasByEntidad();
          setDatosHabitantes(res);
        } 
        else if (activeTab === 'SALARIOS' && datosSalarios.length === 0) {
          const res = await EstadisticaService.getSaldoPromedioEntidad();
          setDatosSalarios(res);
        }
        else if (activeTab === 'HOGARES' && datosHogares.length === 0) {
          const res = await EstadisticaService.getNumHogaresByMunicipio();
          setDatosHogares(res);
        }
        else if (activeTab === 'ESCOLARIDAD' && entidadSeleccionada) {
          const res = await EstadisticaService.getEscolaridadByEntidad(entidadSeleccionada);
          setDatosEscolaridad(res);
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Error al cargar estadísticas');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [activeTab, entidadSeleccionada]); 
  // Se dispara al cambiar de pestaña o al seleccionar una nueva entidad en escolaridad

  // Utilidades de formato
  const formatNumber = (num: number) => new Intl.NumberFormat('es-MX').format(num);
  const formatMoney = (num: number) => new Intl.NumberFormat('es-MX', { style: 'currency', currency: 'MXN' }).format(num);
  
  // Calcula el valor máximo para las barras dinámicas
  const maxHabitantes = Math.max(...datosHabitantes.map(d => d.totalPersonas), 1);
  const maxSalario = Math.max(...datosSalarios.map(d => d.salarioPromedio), 1);
  const maxEscolaridad = Math.max(...datosEscolaridad.map(d => d.totalPersonas), 1);

  return (
    <div className="stats-container">
      <div className="stats-card">
        
        <div className="stats-header">
          <h2>Tablero Estadístico Nacional</h2>
          <p>Indicadores demográficos y socioeconómicos por zona geográfica.</p>
        </div>

        <div className="stats-tabs">
          <button className={`stats-tab-btn ${activeTab === 'HABITANTES' ? 'active' : ''}`} onClick={() => setActiveTab('HABITANTES')}>
            Habitantes por Entidad
          </button>
          <button className={`stats-tab-btn ${activeTab === 'SALARIOS' ? 'active' : ''}`} onClick={() => setActiveTab('SALARIOS')}>
            Salarios Promedio
          </button>
          <button className={`stats-tab-btn ${activeTab === 'HOGARES' ? 'active' : ''}`} onClick={() => setActiveTab('HOGARES')}>
            Hogares por Municipio
          </button>
          <button className={`stats-tab-btn ${activeTab === 'ESCOLARIDAD' ? 'active' : ''}`} onClick={() => setActiveTab('ESCOLARIDAD')}>
            Nivel de Escolaridad
          </button>
        </div>

        {/* Filtro extra solo para la pestaña de Escolaridad */}
        {activeTab === 'ESCOLARIDAD' && (
          <div className="filter-section">
            <label style={{ fontWeight: 500, color: '#374151' }}>Seleccione una Entidad:</label>
            <select 
              value={entidadSeleccionada} 
              onChange={(e) => setEntidadSeleccionada(e.target.value)}
            >
              <option value="">-- Seleccionar --</option>
              {entidades.map(ent => (
                <option key={ent.abreviatura} value={ent.abreviatura}>{ent.nombre}</option>
              ))}
            </select>
          </div>
        )}

        <div className="stats-content">
          {isLoading && <div className="loading-spinner">Generando reporte estadístico...</div>}
          {error && <div className="empty-state" style={{ color: '#ef4444' }}>⚠️ {error}</div>}
          
          {!isLoading && !error && (
            <>
              {/* --- VISTA: HABITANTES --- */}
              {activeTab === 'HABITANTES' && (
                <div>
                  <div className="summary-grid">
                    <div className="summary-item">
                      <div className="summary-value">{formatNumber(datosHabitantes.reduce((acc, curr) => acc + curr.totalPersonas, 0))}</div>
                      <div className="summary-label">Población Total Registrada</div>
                    </div>
                  </div>
                  <table className="admin-table">
                    <thead><tr><th>Entidad Federativa</th><th>Total de Personas</th><th>Proporción</th></tr></thead>
                    <tbody>
                      {datosHabitantes.map(dato => (
                        <tr key={dato.nombre}>
                          <td style={{ width: '30%' }}>{dato.nombre}</td>
                          <td style={{ width: '20%', fontWeight: 600 }}>{formatNumber(dato.totalPersonas)}</td>
                          <td style={{ width: '50%' }}>
                            <div className="data-bar-container">
                              <div className="data-bar-fill" style={{ width: `${(dato.totalPersonas / maxHabitantes) * 100}%` }}></div>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}

              {/* --- VISTA: SALARIOS --- */}
              {activeTab === 'SALARIOS' && (
                <table className="admin-table">
                  <thead><tr><th>Entidad Federativa</th><th>Muestra (Personas)</th><th>Salario Promedio</th><th>Nivel Económico</th></tr></thead>
                  <tbody>
                    {datosSalarios.map(dato => (
                      <tr key={dato.nombreEntidad}>
                        <td style={{ width: '25%' }}>{dato.nombreEntidad}</td>
                        <td style={{ width: '15%' }}>{formatNumber(dato.totalPersona)}</td>
                        <td style={{ width: '20%', fontWeight: 600, color: '#16a34a' }}>{formatMoney(dato.salarioPromedio)}</td>
                        <td style={{ width: '40%' }}>
                          <div className="data-bar-container">
                            <div className="data-bar-fill" style={{ width: `${(dato.salarioPromedio / maxSalario) * 100}%`, backgroundColor: '#16a34a' }}></div>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}

              {/* --- VISTA: HOGARES --- */}
              {activeTab === 'HOGARES' && (
                <table className="admin-table">
                  <thead><tr><th>Municipio</th><th>Cantidad de Hogares</th></tr></thead>
                  <tbody>
                    {datosHogares.map(dato => (
                      <tr key={dato.nombreMunicipio}>
                        <td>{dato.nombreMunicipio}</td>
                        <td style={{ fontWeight: 600 }}>{formatNumber(dato.numHogares)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}

              {/* --- VISTA: ESCOLARIDAD --- */}
              {activeTab === 'ESCOLARIDAD' && (
                <>
                  {!entidadSeleccionada ? (
                    <div className="empty-state">Seleccione una entidad en el menú superior para visualizar sus datos de escolaridad.</div>
                  ) : datosEscolaridad.length === 0 ? (
                    <div className="empty-state">No hay registros de escolaridad para esta entidad.</div>
                  ) : (
                    <table className="admin-table">
                      <thead><tr><th>Nivel de Escolaridad</th><th>Total de Personas</th><th>Distribución</th></tr></thead>
                      <tbody>
                        {datosEscolaridad.map(dato => (
                          <tr key={dato.descripcionEscolaridad}>
                            <td style={{ width: '40%' }}>{dato.descripcionEscolaridad}</td>
                            <td style={{ width: '20%', fontWeight: 600 }}>{formatNumber(dato.totalPersonas)}</td>
                            <td style={{ width: '40%' }}>
                              <div className="data-bar-container">
                                <div className="data-bar-fill" style={{ width: `${(dato.totalPersonas / maxEscolaridad) * 100}%`, backgroundColor: '#eab308' }}></div>
                              </div>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  )}
                </>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default EstadisticasDashboard;