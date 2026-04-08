import React, { useState } from 'react';
import PersonaForm from './pages/Personas/PersonaForm';
import PersonasList from './pages/Personas/PersonasList';
import EstadisticasDashboard from './pages/Estadisticas/EstadisticasDashboard'; // Nuevo import
import './App.css'; 

const App: React.FC = () => {
  // Añadimos 'ESTADISTICAS' a las posibles vistas
  const [vistaActual, setVistaActual] = useState<'REGISTRO' | 'LISTA' | 'ESTADISTICAS'>('ESTADISTICAS');

  return (
    <div className="min-h-screen bg-[#f1f5f9]">

      <main className="app-main">
        
        <div className="app-header">
          <h1 className="app-title">Bienvenido a AdminStats</h1>
          <p className="app-subtitle">Sistema de Gestión Demográfica y Geográfica</p>
        </div>

        <div className="app-nav-buttons">
          <button 
            onClick={() => setVistaActual('REGISTRO')}
            className={`nav-btn ${vistaActual === 'REGISTRO' ? 'active' : 'inactive'}`}
          >
            + Nueva Persona
          </button>

          <button 
            onClick={() => setVistaActual('LISTA')}
            className={`nav-btn ${vistaActual === 'LISTA' ? 'active' : 'inactive'}`}
          >
            Ver Directorio
          </button>

          {/* Nuevo botón para las estadísticas */}
          <button 
            onClick={() => setVistaActual('ESTADISTICAS')}
            className={`nav-btn ${vistaActual === 'ESTADISTICAS' ? 'active' : 'inactive'}`}
          >
            Tablero de Estadísticas
          </button>
        </div>

        {/* Renderizado condicional */}
        {vistaActual === 'REGISTRO' && <PersonaForm />}
        {vistaActual === 'LISTA' && <PersonasList />}
        {vistaActual === 'ESTADISTICAS' && <EstadisticasDashboard />}

      </main>
    </div>
  );
}

export default App;