package com.vecentek.back.util;

import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.DkmKeyLifecycle;
import com.vecentek.back.mapper.DkmKeyLifecycleMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class KeyLifecycleUtil {

    @Resource
    private DkmKeyLifecycleMapper keyLifecycleDao;

    public int insert(DkmKey key, int type, int source, int dkState) {
        // 封装生命周期对象
        DkmKeyLifecycle dkmKeyLifecycle = new DkmKeyLifecycle();
        dkmKeyLifecycle.setUserId(key.getUserId());
        dkmKeyLifecycle.setKeyId(key.getId());
        dkmKeyLifecycle.setVin(key.getVin());
        if (source == 0){
            // 不设置来源
        }else {
            dkmKeyLifecycle.setKeySource(source);
        }
        dkmKeyLifecycle.setKeyType(type);
        dkmKeyLifecycle.setKeyStatus(dkState); // 开通
        dkmKeyLifecycle.setCreateTime(new Date());
        return keyLifecycleDao.insert(dkmKeyLifecycle);
    }

}
