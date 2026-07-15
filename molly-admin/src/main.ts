import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './style.css'
import App from './App.vue'
import router from './router'
import { vPermission } from '@/directives/permission'

const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.directive('permission', vPermission)
app.mount('#app')
