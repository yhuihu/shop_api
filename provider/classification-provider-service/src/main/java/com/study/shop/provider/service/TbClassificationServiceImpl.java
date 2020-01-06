package com.study.shop.provider.service;

import com.study.shop.provider.api.TbClassificationService;
import com.study.shop.provider.domain.TbClassification;
import com.study.shop.provider.mapper.TbClassificationMapper;
import com.study.shop.provider.vo.ClassificationVO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tiger
 * @date 2019-11-19
 * @see com.study.shop.provider.service
 **/
@Service(version = "1.0.0", retries = 0, timeout = 10000)
public class TbClassificationServiceImpl implements TbClassificationService {
    private Lock lock = new ReentrantLock();
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    private final String CLASSIFICATION_KEY = "Classification_";
    private final String CLASSIFICATION_PARENT_KEY = "Classification_Parent_";

    @Resource
    TbClassificationMapper tbClassificationMapper;

    @Override
    public TbClassification getClassificationById(Long id) {
        TbClassification tbClassification = (TbClassification) redisTemplate.opsForValue().get(CLASSIFICATION_KEY + id);
        if (tbClassification == null) {
            tbClassification = tbClassificationMapper.selectByPrimaryKey(id);
            setClassification(tbClassification);
        }
        return tbClassification;
    }

    @Override
    public List<TbClassification> getClassificationList(Long id) {
        Example example = new Example(TbClassification.class);
        example.setOrderByClause("sort_order");
        example.and().andEqualTo("parentId", id);
        return tbClassificationMapper.selectByExample(example);
    }

    @Override
    public List<ClassificationVO> getAllClassification() {
        return tbClassificationMapper.getClassificationList(0L);
    }

    @Override
    public int addClassification(TbClassification tbClassification) {
        if (tbClassification.getSortOrder() == null) {
            tbClassification.setSortOrder(0);
        }
        tbClassification.setStatus(1);
        updateParent(tbClassification.getParentId());
        return tbClassificationMapper.insert(tbClassification);
    }

    @Override
    public int updateClassification(TbClassification tbClassification) {
        return tbClassificationMapper.updateByPrimaryKey(tbClassification);
    }

    @Override
    public void deleteClassification(Long id) {
        deleteTree(id);
        Set<String> longSet = redisTemplate.keys(CLASSIFICATION_PARENT_KEY + "*");
        if (longSet != null) {
            redisTemplate.delete(longSet);
        }
    }

    private void deleteTree(Long id) {
        List<TbClassification> tbClassifications = getClassificationList(id);
        if (tbClassifications.size() > 0) {
            for (TbClassification tbClassification : tbClassifications) {
                deleteTree(tbClassification.getId());
            }
        }
        tbClassificationMapper.deleteByPrimaryKey(id);
    }

    private void updateParent(Long id) {
        if (id != null) {
            redisTemplate.delete(CLASSIFICATION_PARENT_KEY + id);
        }
    }

    private void setClassification(TbClassification tbClassification) {
        if (tbClassification != null) {
            redisTemplate.opsForValue().set(CLASSIFICATION_KEY + tbClassification.getId(), tbClassification);
        }
    }

}
