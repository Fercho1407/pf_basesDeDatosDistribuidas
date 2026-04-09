import { apiFetch } from './api';
import { EntidadFederativa, MunicipioResponseDTO } from '../types';

export const GeografiaService = {

  getEntidadesFederativas: () => {
    return apiFetch<EntidadFederativa[]>('/entidades-federativas', {
      method: 'GET',
    });
  },

  getMunicipiosByEntidad: (abreviatura: string) => {
    const token = localStorage.getItem('jwt');
    return apiFetch<MunicipioResponseDTO[]>(`/municipios-entidad/${abreviatura}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  }

};