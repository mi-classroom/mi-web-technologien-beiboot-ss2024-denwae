import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	server: {
		proxy: {
			'/api': {
				target: 'http://localhost:8080',
				changeOrigin: true,
				rewrite: (path) => path.replace(/^\/api/, '')
			},
			'/images': 'http://localhost:8080',
			'/thumb': 'http://localhost:8080'
		}
	},
	build: {
		sourcemap: true
	}
});
