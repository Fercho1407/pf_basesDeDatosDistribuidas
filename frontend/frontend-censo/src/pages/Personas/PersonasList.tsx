// src/pages/Personas/PersonasList.tsx
import React, { useState, useEffect } from 'react';
import { DatosPersonaDTO, Zona } from '../../types';
import { PersonaService } from '../../services/persona.api';
import './PersonasList.css';

const PersonasList: React.FC = () => {
  const [zonaActiva, setZonaActiva] = useState<Zona>(Zona.URBANA);
  const [personas, setPersonas] = useState<DatosPersonaDTO[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // --- ESTADOS PARA LA MODAL DE EDICIÓN ---
  const [personaEditando, setPersonaEditando] = useState<DatosPersonaDTO | null>(null);
  const [nuevoSalario, setNuevoSalario] = useState<number>(0);
  const [isUpdating, setIsUpdating] = useState<boolean>(false);

  useEffect(() => {
    const fetchPersonas = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const data = await PersonaService.obtenerPersonasZona(zonaActiva);
        setPersonas(data);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Error al cargar los datos');
        setPersonas([]);
      } finally {
        setIsLoading(false);
      }
    };

    fetchPersonas();
  }, [zonaActiva]);

  // --- FUNCIONES DE EDICIÓN ---
  const abrirModalEdicion = (persona: DatosPersonaDTO) => {
    setPersonaEditando(persona);
    setNuevoSalario(persona.ingresoMensual);
  };

  const cerrarModal = () => {
    setPersonaEditando(null);
    setNuevoSalario(0);
  };

  const guardarNuevoSalario = async () => {
    if (!personaEditando) return;
    
    setIsUpdating(true);
    try {
      // Llamamos a tu endpoint @PostMapping("/persona/{zona}/{idPersonaDetalle}/{salario}")
      await PersonaService.actualizarSalario(
        zonaActiva, 
        personaEditando.idPersonaDetalle, 
        nuevoSalario
      );

      // Actualizamos la tabla localmente sin necesidad de volver a consultar a la base de datos
      setPersonas(personasAnteriores => 
        personasAnteriores.map(p => 
          p.idPersonaDetalle === personaEditando.idPersonaDetalle 
            ? { ...p, ingresoMensual: nuevoSalario } 
            : p
        )
      );

      cerrarModal();
      alert('Salario actualizado correctamente');
    } catch (err) {
      alert(`Error al actualizar: ${err instanceof Error ? err.message : 'Desconocido'}`);
    } finally {
      setIsUpdating(false);
    }
  };

  const formatearMoneda = (cantidad: number) => {
    return new Intl.NumberFormat('es-MX', {
      style: 'currency',
      currency: 'MXN'
    }).format(cantidad);
  };

  return (
    <div className="list-container">
      <div className="list-card">
        
        <div className="list-header">
          <h2>Padrón de Habitantes</h2>
        </div>

        <div className="zona-tabs">
          <button className={`tab-button ${zonaActiva === Zona.URBANA ? 'active' : ''}`} onClick={() => setZonaActiva(Zona.URBANA)}>Zona Urbana</button>
          <button className={`tab-button ${zonaActiva === Zona.SUBURBANA ? 'active' : ''}`} onClick={() => setZonaActiva(Zona.SUBURBANA)}>Zona Suburbana</button>
          <button className={`tab-button ${zonaActiva === Zona.RURAL ? 'active' : ''}`} onClick={() => setZonaActiva(Zona.RURAL)}>Zona Rural</button>
        </div>

        <div className="table-responsive">
          {isLoading ? (
            <div className="loading-spinner">Consultando base de datos...</div>
          ) : error ? (
            <div className="empty-state" style={{ color: '#ef4444' }}>⚠️ {error}</div>
          ) : personas.length === 0 ? (
            <div className="empty-state">No hay registros de personas en esta zona.</div>
          ) : (
            <table className="admin-table">
              <thead>
                <tr>
                  <th>CURP</th>
                  <th>Nombre Completo</th>
                  <th>Dirección</th>
                  <th>Ingreso Mensual</th>
                  <th style={{ textAlign: 'center' }}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {personas.map((persona) => (
                  <tr key={persona.idPersonaDetalle}>
                    <td style={{ fontFamily: 'monospace' }}>{persona.curp}</td>
                    <td>{persona.nombre} {persona.apellidoPaterno} {persona.apellidoMaterno}</td>
                    <td>{persona.direccion} #{persona.numeroInterno || 'S/N'}</td>
                    <td style={{ fontWeight: 500, color: '#16a34a' }}>
                      {formatearMoneda(persona.ingresoMensual)}
                    </td>
                    <td style={{ textAlign: 'center' }}>
                      <button 
                        className="btn-edit"
                        onClick={() => abrirModalEdicion(persona)}
                      >
                        Editar Salario
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* --- MODAL DE EDICIÓN --- */}
      {personaEditando && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>Actualizar Salario</h3>
              <button className="btn-close" onClick={cerrarModal}>&times;</button>
            </div>
            
            <div className="modal-body">
              <p style={{ fontSize: '0.875rem', color: '#64748b', marginBottom: '1rem' }}>
                Editando datos de: <strong>{personaEditando.nombre} {personaEditando.apellidoPaterno}</strong>
              </p>
              
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                <label style={{ fontSize: '0.875rem', fontWeight: 500, color: '#374151' }}>
                  Nuevo Ingreso Mensual ($)
                </label>
                <input 
                  type="number" 
                  step="0.01"
                  min="0"
                  value={nuevoSalario}
                  onChange={(e) => setNuevoSalario(Number(e.target.value))}
                  style={{ 
                    padding: '0.5rem', 
                    borderRadius: '0.375rem', 
                    border: '1px solid #d1d5db',
                    fontSize: '1rem'
                  }}
                />
              </div>
            </div>

            <div className="modal-footer">
              <button className="btn-cancel" onClick={cerrarModal} disabled={isUpdating}>
                Cancelar
              </button>
              <button className="btn-save" onClick={guardarNuevoSalario} disabled={isUpdating}>
                {isUpdating ? 'Guardando...' : 'Guardar Cambios'}
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default PersonasList;