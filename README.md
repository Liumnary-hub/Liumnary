# 企业级进销存管理系统 API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-green)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-7.0-red)](https://redis.io/)

## 📌 项目简介
本项目是一个基于 Spring Boot 的后台管理系统，涵盖商品管理、库存调度、员工权限及销售数据统计。重点解决了高并发场景下的**接口防刷**与**数据一致性**问题。

## ✨ 核心亮点（面试重点）
- **自定义注解式限流**：基于 AOP + Redis 原子自增，实现对登录等敏感接口的 IP 级别防暴力破解。
- **Redis 分布式锁**：手写 Lua 脚本实现 `SET NX EX` 原子锁，解决商品修改/删除时的并发数据覆盖问题。
- **统一响应与异常处理**：通过 `@RestControllerAdvice` 封装统一返回体，规范 API 格式。
- **JWT 无状态认证**：集成 Spring Security 实现 Token 鉴权。

## 🛠 技术栈
| 分类 | 技术 |
|------|------|
| 核心框架 | Spring Boot, Spring Data JPA |
| 安全控制 | Spring Security, JWT |
| 缓存与并发 | Redis (Lettuce), AOP |
| 数据库 | MySQL |
| 工具库 | Lombok, UUID |

## 🚀 快速开始
1. 克隆项目
   ```bash
   git clone https://github.com/Liumnary-hub/Liumnary.git
   我只上传了后端的代码，前端的代码还没有上传
