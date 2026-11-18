# 企业通讯录管理系统 · 前端

基于 Vue 3 + TypeScript + Vite 的管理端，按角色分为超级管理员（`/boss`）、管理员（`/admin`）、员工（`/employee`）三套界面。

完整的项目介绍、后端启动方式和演示账号见仓库根目录的 [README](../README.md)。

## 开发

```bash
npm install
npm run dev      # 默认 http://localhost:5173
npm run build    # 类型检查 + 生产构建
```

## 配置

后端接口地址在 `src/api/config.ts` 的 `API_CONFIG.BASE_URL`，默认 `http://localhost:8082/wei`。
