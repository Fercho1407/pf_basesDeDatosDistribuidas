import { apiFetch } from './api';
import { PersonaCreateDTO, PersonaResponseDTO, DatosPersonaDTO, Zona } from '../types';

export const PersonaService = {
  
  guardarPersona: (persona: PersonaCreateDTO) => {
    return apiFetch<PersonaResponseDTO>('/persona', {
      method: 'POST',
      body: JSON.stringify(persona),
    });
  },

  obtenerPersonasZona: (zona: Zona) => {
    return apiFetch<DatosPersonaDTO[]>(`/personas/${zona}`, {
      method: 'GET',
    });
  },

  actualizarSalario: (zona: Zona, idPersonaDetalle: number, salario: number) => {
    return apiFetch<number>(`/persona/${zona}/${idPersonaDetalle}/${salario}`, {
      method: 'POST',
    });
  }
  
};