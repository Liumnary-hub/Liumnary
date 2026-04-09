package com.example.api.service.impl;

import com.example.api.exception.BusinessException;
import com.example.api.model.entity.Commodity;
import com.example.api.repository.CommodityRepository;
import com.example.api.service.CommodityService;
import com.example.api.utils.RedisLockUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Resource
    private CommodityRepository commodityRepository;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisLockUtil redisLockUtil;

    // ====================== 添加商品（带锁） ======================
    @Override
    public Commodity save(Commodity commodity) {
        String lockKey = "lock:commodity:add:" + commodity.getName();
        String requestId = UUID.randomUUID().toString();

        try {
            // 加锁 10 秒
            boolean lock = redisLockUtil.lock(stringRedisTemplate, lockKey, requestId, 10);
            if (!lock) {
                throw new BusinessException("操作频繁，请稍后再试");
            }

            // 防止重复添加同名商品
            if (commodityRepository.existsByName(commodity.getName())) {
                throw new BusinessException("商品名称已存在");
            }

            return commodityRepository.save(commodity);
        } finally {
            redisLockUtil.unlock(stringRedisTemplate, lockKey, requestId);
        }
    }

    // ====================== 修改商品（带锁） ======================
    @Override
    public void update(Commodity commodity) {
        String lockKey = "lock:commodity:update:" + commodity.getId();
        String requestId = UUID.randomUUID().toString();

        try {
            boolean lock = redisLockUtil.lock(stringRedisTemplate, lockKey, requestId, 10);
            if (!lock) {
                throw new BusinessException("操作频繁，请稍后再试");
            }

            // 判断商品是否存在
            Commodity old = commodityRepository.findById(commodity.getId()).orElse(null);
            if (old == null) {
                throw new BusinessException("商品不存在");
            }

            commodityRepository.save(commodity);
        } finally {
            redisLockUtil.unlock(stringRedisTemplate, lockKey, requestId);
        }
    }

    // ====================== 删除商品（带锁） ======================
    @Override
    public void delete(String id) {
        String lockKey = "lock:commodity:delete:" + id;
        String requestId = UUID.randomUUID().toString();

        try {
            boolean lock = redisLockUtil.lock(stringRedisTemplate, lockKey, requestId, 10);
            if (!lock) {
                throw new BusinessException("操作频繁，请稍后再试");
            }

            commodityRepository.deleteById(id);
        } finally {
            redisLockUtil.unlock(stringRedisTemplate, lockKey, requestId);
        }
    }

    // ====================== 以下不需要加锁 ======================
    @Override
    public Commodity findById(String id) {
        return commodityRepository.findById(id).orElse(null);
    }

    @Override
    public List<Commodity> findAll() {
        return commodityRepository.findAll();
    }

    @Override
    public List<Commodity> findAllByLikeName(String name) {
        return commodityRepository.findByNameContaining(name);
    }
}