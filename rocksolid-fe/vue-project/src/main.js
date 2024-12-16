import { createApp } from 'vue'
import { createPinia } from 'pinia'
import 'bootstrap/dist/css/bootstrap.min.css';

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

// Components
import App from './App.vue'
import router from './router'

const vuetify = createVuetify({
    components,
    directives,
    icons: {
        defaultSet: 'mdi',
    }
})

createApp(App).use(vuetify).use(router).use(createPinia()).mount('#app')