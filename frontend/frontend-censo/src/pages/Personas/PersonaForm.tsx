// src/pages/Personas/PersonaForm.tsx
import React, { useState, useEffect } from 'react';
import { PersonaCreateDTO, Zona, EntidadFederativa, MunicipioResponseDTO } from '../../types';
import { PersonaService } from '../../services/persona.api';
import { GeografiaService } from '../../services/geografia.api';
import './PersonaForm.css';

// 1. Extraemos el estado inicial para poder resetear el formulario fácilmente
const INITIAL_FORM_STATE: PersonaCreateDTO = {
  idMunicipio: 0,
  nombrelocalida: '',
  tipoLocalidad: Zona.URBANA,
  direccion: '',
  tipoVivienda: '',
  materialPared: '',
  materialTecho: '',
  numeroExterno: '',
  servicioAgua: 'NO',
  servicioLuz: 'NO',
  numeroInterno: '',
  tipoHogar: '',
  curp: '',
  nombrePersona: '',
  sexoPersona: '',
  edadPersona: 0,
  parentezco: '',
  esJefeHogar: 'NO',
  apellidoMaterno: '',
  apellidoPaterno: '',
  ingresoMensual: 0,
  hablaLenguaIndigena: 'NO',
  descripcionOcupacion: '',
  descripcionEscolaridad: ''
};

const PersonaForm: React.FC = () => {
  // --- ESTADOS DEL FORMULARIO ---
  const [formData, setFormData] = useState<PersonaCreateDTO>(INITIAL_FORM_STATE);
  const [isLoading, setIsLoading] = useState(false);

  // --- ESTADOS PARA LOS COMBOBOX (CATÁLOGOS) ---
  const [entidades, setEntidades] = useState<EntidadFederativa[]>([]);
  const [municipios, setMunicipios] = useState<MunicipioResponseDTO[]>([]);
  const [entidadSeleccionada, setEntidadSeleccionada] = useState<string>('');

  // 2. Cargar Entidades Federativas al montar el componente
  useEffect(() => {
    const fetchEntidades = async () => {
      try {
        const data = await GeografiaService.getEntidadesFederativas();
        setEntidades(data);
      } catch (error) {
        console.error('Error al cargar entidades federativas', error);
      }
    };
    fetchEntidades();
  }, []);

  // 3. Cargar Municipios cada vez que cambie la Entidad seleccionada
  useEffect(() => {
    if (entidadSeleccionada) {
      const fetchMunicipios = async () => {
        try {
          const data = await GeografiaService.getMunicipiosByEntidad(entidadSeleccionada);
          setMunicipios(data);
        } catch (error) {
          console.error('Error al cargar municipios', error);
        }
      };
      fetchMunicipios();
    } else {
      // Si no hay entidad seleccionada, limpiamos los municipios
      setMunicipios([]);
      setFormData(prev => ({ ...prev, idMunicipio: 0 }));
    }
  }, [entidadSeleccionada]);


  // --- MANEJADORES DE EVENTOS ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    const isNumericField = ['idMunicipio', 'edadPersona', 'ingresoMensual'].includes(name);

    setFormData(prev => ({
      ...prev,
      [name]: isNumericField ? (value === '' ? 0 : Number(value)) : value
    }));
  };

  // Manejador especial para el select de Entidad
  const handleEntidadChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setEntidadSeleccionada(e.target.value);
    // Al cambiar la entidad, borramos el municipio seleccionado previamente
    setFormData(prev => ({ ...prev, idMunicipio: 0 }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    // Validación básica
    if (formData.idMunicipio === 0) {
      alert("Por favor, seleccione un municipio válido.");
      return;
    }

    setIsLoading(true);
    try {
      const respuesta = await PersonaService.guardarPersona(formData);
      alert(`¡Registro exitoso! Se guardó a ${respuesta.nombre} ${respuesta.apellidoPaterno}`);
      
      // Limpieza total del formulario después de guardar exitosamente
      setFormData(INITIAL_FORM_STATE);
      setEntidadSeleccionada('');
      
    } catch (error) {
      alert(`Error al guardar: ${error instanceof Error ? error.message : 'Error desconocido'}`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="form-container">
      <div className="form-card">
        
        <div className="form-header">
          <h2>Registro de Persona y Hogar</h2>
          <p>Complete la información demográfica, geográfica y de vivienda.</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-body">
            
            {/* SECCIÓN 1: IDENTIFICACIÓN PERSONAL */}
            <div className="form-section">
              <h3 className="form-section-title">1. Identificación Personal</h3>
              <div className="form-grid">
                <div className="form-group">
                  <label className="form-label">Nombre(s)</label>
                  <input required type="text" name="nombrePersona" value={formData.nombrePersona} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Apellido Paterno</label>
                  <input required type="text" name="apellidoPaterno" value={formData.apellidoPaterno} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Apellido Materno</label>
                  <input type="text" name="apellidoMaterno" value={formData.apellidoMaterno} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">CURP</label>
                  <input required type="text" name="curp" value={formData.curp} onChange={handleChange} className="form-input" maxLength={18} />
                </div>
                <div className="form-group">
                  <label className="form-label">Edad</label>
                  <input required type="number" min="0" name="edadPersona" value={formData.edadPersona} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Sexo</label>
                  <select required name="sexoPersona" value={formData.sexoPersona} onChange={handleChange} className="form-select">
                    <option value="">Seleccione...</option>
                    <option value="H">Hombre</option>
                    <option value="M">Mujer</option>
                  </select>
                </div>
              </div>
            </div>

            {/* SECCIÓN 2: UBICACIÓN GEOGRÁFICA */}
            <div className="form-section">
              <h3 className="form-section-title">2. Ubicación Geográfica</h3>
              <div className="form-grid">
                
                <div className="form-group">
                  <label className="form-label">Entidad Federativa</label>
                  <select 
                    required 
                    className="form-select"
                    value={entidadSeleccionada}
                    onChange={handleEntidadChange}
                  >
                    <option value="">Seleccione una entidad...</option>
                    {entidades.map(entidad => (
                      <option key={entidad.abreviatura} value={entidad.abreviatura}>
                        {entidad.nombre}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label className="form-label">Municipio</label>
                  <select 
                    required 
                    name="idMunicipio"
                    className="form-select"
                    value={formData.idMunicipio === 0 ? '' : formData.idMunicipio}
                    onChange={handleChange}
                    disabled={!entidadSeleccionada || municipios.length === 0}
                  >
                    <option value="">Seleccione un municipio...</option>
                    {municipios.map(mun => (
                      <option key={mun.idMunicipio} value={mun.idMunicipio}>
                        {mun.nombreMunicipio}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label className="form-label">Nombre Localidad</label>
                  <input required type="text" name="nombrelocalida" value={formData.nombrelocalida} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Zona</label>
                  <select required name="tipoLocalidad" value={formData.tipoLocalidad} onChange={handleChange} className="form-select">
                    <option value={Zona.RURAL}>Rural</option>
                    <option value={Zona.SUBURBANA}>Suburbana</option>
                    <option value={Zona.URBANA}>Urbana</option>
                  </select>
                </div>
              </div>
            </div>

            {/* SECCIÓN 3: VIVIENDA Y HOGAR */}
            <div className="form-section">
              <h3 className="form-section-title">3. Características de la Vivienda y Hogar</h3>
              <div className="form-grid">
                <div className="form-group col-span-2">
                  <label className="form-label">Dirección (Calle)</label>
                  <input required type="text" name="direccion" value={formData.direccion} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Número Externo</label>
                  <input required type="text" name="numeroExterno" value={formData.numeroExterno} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Número Interno</label>
                  <input type="text" name="numeroInterno" value={formData.numeroInterno} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Tipo de Vivienda</label>
                  <input required type="text" name="tipoVivienda" value={formData.tipoVivienda} onChange={handleChange} className="form-input" placeholder="Ej. Casa sola, Departamento..." />
                </div>
                <div className="form-group">
                  <label className="form-label">Tipo de Hogar</label>
                  <input required type="text" name="tipoHogar" value={formData.tipoHogar} onChange={handleChange} className="form-input" placeholder="Ej. Nuclear, Ampliado..." />
                </div>
                <div className="form-group">
                  <label className="form-label">Material Pared</label>
                  <input required type="text" name="materialPared" value={formData.materialPared} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Material Techo</label>
                  <input required type="text" name="materialTecho" value={formData.materialTecho} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Servicio de Agua</label>
                  <select required name="servicioAgua" value={formData.servicioAgua} onChange={handleChange} className="form-select">
                    <option value="SI">Sí cuenta con servicio</option>
                    <option value="NO">No cuenta con servicio</option>
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Servicio de Luz</label>
                  <select required name="servicioLuz" value={formData.servicioLuz} onChange={handleChange} className="form-select">
                    <option value="SI">Sí cuenta con servicio</option>
                    <option value="NO">No cuenta con servicio</option>
                  </select>
                </div>
              </div>
            </div>

            {/* SECCIÓN 4: DETALLES SOCIOECONÓMICOS */}
            <div className="form-section">
              <h3 className="form-section-title">4. Detalles Socioeconómicos</h3>
              <div className="form-grid">
                <div className="form-group">
                  <label className="form-label">Ingreso Mensual ($)</label>
                  <input required type="number" step="0.01" min="0" name="ingresoMensual" value={formData.ingresoMensual} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Ocupación</label>
                  <input required type="text" name="descripcionOcupacion" value={formData.descripcionOcupacion} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">Escolaridad</label>
                  <input required type="text" name="descripcionEscolaridad" value={formData.descripcionEscolaridad} onChange={handleChange} className="form-input" />
                </div>
                <div className="form-group">
                  <label className="form-label">¿Habla Lengua Indígena?</label>
                  <select required name="hablaLenguaIndigena" value={formData.hablaLenguaIndigena} onChange={handleChange} className="form-select">
                    <option value="NO">No</option>
                    <option value="SI">Sí</option>
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Parentesco con el Jefe(a)</label>
                  <input required type="text" name="parentezco" value={formData.parentezco} onChange={handleChange} className="form-input" placeholder="Ej. Hijo, Cónyuge..." />
                </div>
                <div className="form-group">
                  <label className="form-label">¿Es Jefe(a) de Hogar?</label>
                  <select required name="esJefeHogar" value={formData.esJefeHogar} onChange={handleChange} className="form-select">
                    <option value="NO">No</option>
                    <option value="SI">Sí</option>
                  </select>
                </div>
              </div>
            </div>

          </div>

          <div className="form-actions">
            <button type="submit" disabled={isLoading} className="btn-submit">
              {isLoading ? 'Procesando...' : 'Guardar Registro'}
            </button>
          </div>
        </form>

      </div>
    </div>
  );
};

export default PersonaForm;