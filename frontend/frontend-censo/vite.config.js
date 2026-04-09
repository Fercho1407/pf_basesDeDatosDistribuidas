import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],

  preview: {
    allowedHosts: ['los-abstractos.online'],
    port: 4173,
    host: true
  },
  server: {
    allowedHosts: ['los-abstractos.online'],
    host: true
  }
})
