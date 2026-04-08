import { apiFetch } from './api';
import { 
  ConteoPersonasEntidadDTO, 
  SueldoPromedioEntidadDTO, 
  HogaresPorMunicipioDTO, 
  EscolaridadPorEntidadDTO 
} from '../types';

export const EstadisticaService = {

  getNumPersonasByEntidad: () => {
    return apiFetch<ConteoPersonasEntidadDTO[]>('/habitantes-entidades', {
      method: 'GET',
    });
  },

  getSaldoPromedioEntidad: () => {
    return apiFetch<SueldoPromedioEntidadDTO[]>('/salario-promedio', {
      method: 'GET',
    });
  },

  getNumHogaresByMunicipio: () => {
    return apiFetch<HogaresPorMunicipioDTO[]>('/hogares-municipio', {
      method: 'GET',
    });
  },

  getEscolaridadByEntidad: (abreviatura: string) => {
    return apiFetch<EscolaridadPorEntidadDTO[]>(`/escolaridad-entidad/${abreviatura}`, {
      method: 'GET',
    });
  }

};