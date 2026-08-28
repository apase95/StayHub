/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/js/**/*.js"
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          500: '#FF6B6B',
          600: '#E85555',
        },
        text: {
          900: '#111827',
          gray: '#6B7280',
        },
        bg: {
          white: '#FFFFFF',
          light: '#F9FAFB',
        },
        border: '#E5E7EB',
        success: {
          500: '#16A34A',
        },
        warning: {
          500: '#D97706',
        },
        rating: {
          500: '#F59E0B',
        }
      },
      fontFamily: {
        sans: ['Inter', 'Helvetica Neue', 'sans-serif'],
      },
    },
  },
  plugins: [],
}