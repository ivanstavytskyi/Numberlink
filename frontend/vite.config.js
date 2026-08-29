import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'

const rootDir = dirname(fileURLToPath(import.meta.url))
const backend = process.env.BACKEND_PROXY || 'http://127.0.0.1:8000'

const backendProxy = {
  '/api': { target: backend, changeOrigin: true },
  '/oauth2': { target: backend, changeOrigin: true },
  '/login': { target: backend, changeOrigin: true },
  '/uploads': { target: backend, changeOrigin: true },
}

export default defineConfig({
  root: 'src',
  build: {
    outDir: resolve(rootDir, 'dist'),
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(rootDir, 'src/index.html'),
        leaderboard: resolve(rootDir, 'src/leaderboard/index.html'),
        reviews: resolve(rootDir, 'src/reviews/index.html'),
        faqs: resolve(rootDir, 'src/faqs/index.html'),
        verify: resolve(rootDir, 'src/verify/index.html'),
      },
    },
  },
  preview: {
    port: 7000,
    strictPort: true,
    host: '0.0.0.0',
    proxy: backendProxy,
  },
  server: {
    port: 7000,
    strictPort: true,
    host: '0.0.0.0',
    proxy: backendProxy,
    watch: {
      usePolling: true,
      interval: 300,
    },
  },
})
