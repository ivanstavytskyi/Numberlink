import { defineConfig } from 'vite'

export default defineConfig({
  root: "src",
  server: {
    port: 7000,
    strictPort: true,
    host: "0.0.0.0",
    watch: {
      usePolling: true,
      interval: 300,
    },
  },
})