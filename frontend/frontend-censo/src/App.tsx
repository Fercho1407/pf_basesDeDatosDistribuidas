import React, { useState, useEffect } from 'react';
import PersonaForm from './pages/Personas/PersonaForm';
import PersonasList from './pages/Personas/PersonasList';
import EstadisticasDashboard from './pages/Estadisticas/EstadisticasDashboard';
import LoginForm from './pages/Auth/LoginForm';
import './App.css'; 

// Añadimos 'LOGIN' a las vistas posibles
type Vista = 'REGISTRO' | 'LISTA' | 'ESTADISTICAS' | 'LOGIN';

const App: React.FC = () => {
  const [vistaActual, setVistaActual] = useState<Vista>('ESTADISTICAS');
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  // Verificar si ya hay un token guardado
  useEffect(() => {
    const token = localStorage.getItem('jwt');
    if (token) {
      setIsAuthenticated(true);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('jwt');
    setIsAuthenticated(false);
    setVistaActual('ESTADISTICAS'); // Redirigir a la vista pública por seguridad
  };

  return (
    <div className="min-h-screen bg-[#f1f5f9]">
      <main className="app-main">
        
        <div className="app-header flex justify-between items-center p-4">
          <div>
            <h1 className="app-title text-2xl font-bold">Bienvenido a AdminStats</h1>
            <p className="app-subtitle text-gray-600">Sistema de Gestión Demográfica y Geográfica</p>
          </div>
          
          {/* Botón de Login */}
          <div>
            {isAuthenticated ? (
              <button 
                onClick={handleLogout}
                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow transition-colors"
              >
                Cerrar Sesión
              </button>
            ) : (
              <button 
                onClick={() => setVistaActual('LOGIN')}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded shadow transition-colors"
              >
                Iniciar Sesión
              </button>
            )}
          </div>
        </div>

        <div className="app-nav-buttons flex gap-4 my-6 p-4 bg-white shadow rounded">
          <button 
            onClick={() => setVistaActual('ESTADISTICAS')}
            className={`nav-btn px-4 py-2 rounded ${vistaActual === 'ESTADISTICAS' ? 'bg-gray-200 font-bold' : 'hover:bg-gray-100'}`}
          >
            Tablero de Estadísticas
          </button>

          {/* Mostrar si el usuario está loggeado */}
          {isAuthenticated && (
            <>
              <button 
                onClick={() => setVistaActual('REGISTRO')}
                className={`nav-btn px-4 py-2 rounded ${vistaActual === 'REGISTRO' ? 'bg-gray-200 font-bold' : 'hover:bg-gray-100'}`}
              >
                + Nueva Persona
              </button>

              <button 
                onClick={() => setVistaActual('LISTA')}
                className={`nav-btn px-4 py-2 rounded ${vistaActual === 'LISTA' ? 'bg-gray-200 font-bold' : 'hover:bg-gray-100'}`}
              >
                Ver Directorio
              </button>
            </>
          )}
        </div>

        <div className="app-content p-4">
          {vistaActual === 'ESTADISTICAS' && <EstadisticasDashboard />}
          {vistaActual === 'REGISTRO' && isAuthenticated && <PersonaForm />}
          {vistaActual === 'LISTA' && isAuthenticated && <PersonasList />}
          {vistaActual === 'LOGIN' && !isAuthenticated && (
            <LoginForm 
              onLoginSuccess={() => {
                setIsAuthenticated(true);
                setVistaActual('LISTA'); 
              }} 
            />
          )}
        </div>

      </main>
    </div>
  );
}

export default App;