package com.vecentek.back.util;


import com.vecentek.back.mapper.AutoCreatTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liujz
 * @date 2022/8/8
 * @apiNote
 */

public class CreatTableUtil {
    /**
     * 日活表前缀
     */
    public static final String USER_ACTIVE_TABLE_PREFIX = "dkm_key_";

    @Autowired(required = false)
    AutoCreatTableMapper autoCreatTable;

    /**
     * 定时器(每天凌晨1点执行一次)
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/30 0/1 * * * ? ")
    public void createTableJob() {
        // 每次创建多张表（如果一次创建一张表，可能会有问题，比如由于未知原因，导致定时任务未执行，或者执行出错，此时表会创建失败）
        int num = 3;

        for(int i=1; i<=num; i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, i);

            createTable(c.getTime());
        }
    }

    /**
     * 获取表名
     * @param date
     * @return
     */
    public String getTableName(Date date) {
        return USER_ACTIVE_TABLE_PREFIX + new SimpleDateFormat("yyyyMM").format(date);
    }

    /**
     * 创建表
     * @param date
     */
    public void createTable(Date date) {
        String sql = "create table if not exists " + getTableName(date) +
                "(\n" +
                "  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钥匙id',\n" +
                "  `vehicle_id` int(11) NOT NULL COMMENT '车辆ID',\n" +
                "  `user_id` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '钥匙用户ID',\n" +
                "  `phone_fingerprint` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机设备指纹',\n" +
                "  `vin` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '车架号',\n" +
                "  `dk_state` int(11) NOT NULL COMMENT '数字钥匙的具体状态(1 已启用，3 冻结 ，4 过期， 5 吊销) ',\n" +
                "  `val_from` datetime(0) DEFAULT NULL COMMENT '数字钥匙生效时间',\n" +
                "  `val_to` datetime(0) DEFAULT NULL COMMENT '数字钥匙失效时间',\n" +
                "  `permissions` int(11) NOT NULL COMMENT '授权权限值:  1 前门 2 后门 4 尾箱 8 启动 16 车机',\n" +
                "  `server_sig` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '签名值: 用服务端私钥对信息( server_cnt+user_id+手机设备指纹+VIN号+key_id+dk_state+val_from+val_to+permit+server_info) 做RSA2048的SHA256位的签名算法，得到签名值，转成Base64字符串，320位',\n" +
                "  `apply_time` datetime(0) NOT NULL COMMENT '申请时间',\n" +
                "  `activate_times` int(11) DEFAULT NULL COMMENT '离线可用操作次数',\n" +
                "  `random` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '随机数',\n" +
                "  `flag` int(10) UNSIGNED ZEROFILL DEFAULT NULL COMMENT '是否激活；0未激活 1已激活',\n" +
                "  `parent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父钥匙ID',\n" +
                "  `personal_and_calibration` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '个性化标定数据',\n" +
                "  `ble_mac_address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '蓝牙mac地址',\n" +
                "  `kr` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '钥匙信息kr',\n" +
                "  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',\n" +
                "  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',\n" +
                "  `updator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '编辑者',\n" +
                "  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建者',\n" +
                "  `ks` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,\n" +
                "  `period` bigint(11) DEFAULT NULL COMMENT '钥匙周期',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  INDEX `pk-userid-index`(`user_id`) USING BTREE,\n" +
                "  INDEX `pk-vehicleid-index`(`vehicle_id`) USING BTREE,\n" +
                "  INDEX `pk-vin-index`(`vin`) USING BTREE\n" +
                ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '钥匙信息' ROW_FORMAT = Dynamic;"
                ;
        System.out.println(sql);
        autoCreatTable.createTable(sql);

    }
}

