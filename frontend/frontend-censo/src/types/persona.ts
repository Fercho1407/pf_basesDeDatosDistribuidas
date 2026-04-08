export enum Zona {
  RURAL = 'RURAL',
  SUBURBANA = 'SUBURBANA',
  URBANA = 'URBANA'
}

export interface PersonaCreateDTO {
  // Municipio
  idMunicipio: number;
  
  // Localidad
  nombrelocalida: string; 
  tipoLocalidad: Zona;
  
  // Vivienda
  direccion: string;
  tipoVivienda: string;
  materialPared: string;
  materialTecho: string;
  numeroExterno: string;
  servicioAgua: string;
  servicioLuz: string;
  
  // Hogar
  numeroInterno: string;
  tipoHogar: string;
  
  // Persona
  curp: string;
  nombrePersona: string;
  sexoPersona: string;
  edadPersona: number;
  parentezco: string;
  esJefeHogar: string;
  apellidoMaterno: string;
  apellidoPaterno: string;
  
  // Persona Detalle
  ingresoMensual: number;
  hablaLenguaIndigena: string;
  
  // Ocupacion
  descripcionOcupacion: string;
  
  // Escolaridad
  descripcionEscolaridad: string;
}

export interface PersonaResponseDTO {
  nombre: string;
  apellidoPaterno: string;
}

export interface DatosPersonaDTO {
  idLocalidad: number;
  tipoLocalidad: Zona;
  direccion: string;
  numeroInterno: string;
  curp: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno: string;
  sexo: string;
  idPersonaDetalle: number;
  ingresoMensual: number;
}