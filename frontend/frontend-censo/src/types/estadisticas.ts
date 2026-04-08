export interface ConteoPersonasEntidadDTO {
  nombre: string;
  totalPersonas: number;
}

export interface EscolaridadPorEntidadDTO {
  descripcionEscolaridad: string;
  totalPersonas: number;
}

export interface HogaresPorMunicipioDTO {
  nombreMunicipio: string;
  numHogares: number;
}

export interface SueldoPromedioEntidadDTO {
  nombreEntidad: string;
  salarioPromedio: number;
  totalPersona: number;
}