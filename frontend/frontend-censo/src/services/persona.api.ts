import { apiFetch } from './api';
import { PersonaCreateDTO, PersonaResponseDTO, DatosPersonaDTO, Zona } from '../types';

export const PersonaService = {
  
  guardarPersona: (persona: PersonaCreateDTO) => {
    const token = localStorage.getItem('jwt'); // Se lee justo al ejecutar
    return apiFetch<PersonaResponseDTO>('/persona', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json' // Siempre es buena práctica indicarlo en los POST
      },
      body: JSON.stringify(persona),
    });
  },

  obtenerPersonasZona: (zona: Zona) => {
    const token = localStorage.getItem('jwt');
    return apiFetch<DatosPersonaDTO[]>(`/personas/${zona}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  },

  actualizarSalario: (zona: Zona, idPersonaDetalle: number, salario: number) => {
    const token = localStorage.getItem('jwt');
    return apiFetch<number>(`/persona/${zona}/${idPersonaDetalle}/${salario}`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
  }
  
};