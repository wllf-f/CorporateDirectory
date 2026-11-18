import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// @ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// 调试：检查路由是否正确注册
console.log('Routes registered:', router.getRoutes().map(r => ({ path: r.path, name: r.name })))
app.use(ElementPlus, {
  locale: zhCn,
})

app.mount('#app')
