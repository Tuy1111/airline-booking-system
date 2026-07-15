import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const backendHost = (env.VITE_BACKEND_HOST ?? 'http://localhost').replace(/\/+$/, '')
  const serviceTarget = (envKey: string, port: number) => ({
    target: env[envKey] ?? `${backendHost}:${port}`,
    changeOrigin: true,
  })

  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api/v1/flights': serviceTarget('VITE_FLIGHT_PROXY_URL', 8081),
        '/api/v1/airports': serviceTarget('VITE_FLIGHT_PROXY_URL', 8081),
        '/api/v1/airlines': serviceTarget('VITE_FLIGHT_PROXY_URL', 8081),
        '/api/v1/routes': serviceTarget('VITE_FLIGHT_PROXY_URL', 8081),
        '/api/v1/bookings': serviceTarget('VITE_BOOKING_PROXY_URL', 8082),
        '/bookings': serviceTarget('VITE_BOOKING_PROXY_URL', 8082),
        '/api/v1/users': serviceTarget('VITE_USER_PROXY_URL', 8083),
        '/api/v1/payments': serviceTarget('VITE_PAYMENT_PROXY_URL', 8084),
        '/api/v1/notifications': serviceTarget('VITE_NOTIFICATION_PROXY_URL', 8085),
      },
    },
  }
})
