package com.vecentek.back.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vecentek.back.entity.DkmKey;
import com.vecentek.back.entity.Dome;
import com.vecentek.back.mapper.DomeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 */
@Api(tags = "分库分表、读写分离")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/testTable")
public class TestController {

    private final DomeMapper domeMapper;

    //shardingsphere 不会自己创建表， 需要自己编写 根据时间范围创建表的方法
    //分表查询过程中表要是不存在会报错，必须提前生成对应的表

    @ApiOperation("分表后查询")
    @GetMapping("/list")
    public Object list() {

        //LambdaQueryWrapper<Dome> between = new QueryWrapper<Dome>().lambda()
        //        .eq(Dome::getUpdateId, 213)
        //        .between(Dome::getCreateDate, "2021-09-01 00:00:00", "2021-11-30 23:59:59");
        //List<Dome> domes = domeMapper.selectList(between);
        //
        ////分表后查询必须要使用 between进行查询
        //return domes;
        return domeMapper.selectPage(new Page<Dome>(1, 15), new QueryWrapper<Dome>().lambda()
                .eq(Dome::getUpdateId, 213)
                .between(Dome::getCreateDate, "2021-01-01 00:00:00", "2022-03-01 23:59:59"));
    }

    @ApiOperation("分表后添加")
    @GetMapping("/add")
    public Object add() throws ParseException {
        //分表是跟进创建时间， 创建时间必须要有值， 也可以使用mybatis的自动填充功能
        Dome entity = new Dome();
        entity.setId(IdUtil.getSnowflakeNextId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2021-09-01 00:00:00");
        entity.setCreateDate(date);

        entity.setCreateDate(new Date());
        entity.setTitle(DateUtil.date().toString("yyyy-MM-dd HH:mm:ss"));
        entity.setCont(entity.getTitle());
        domeMapper.insert(entity);
        return entity;    }

    @ApiOperation("分表后更新")
    @GetMapping("/update")
    public Object update() {
        //分表后更新， 必须要知道对应数据的创建年月，也就是between才能出发分表的操作
        Dome entity = new Dome();
        entity.setId(1513013357774950400L);
        entity.setTitle("更新标题" + DateUtil.date().toString("yyyy-MM-dd HH:mm:ss"));
        domeMapper.update(entity, new QueryWrapper<Dome>().lambda()
                .between(Dome::getCreateDate, "2022-04-01 00:00:00", "2022-04-30 23:59:59"));
        return entity;
    }

    @ApiOperation("分表后删除")
    @GetMapping("/delete")
    public Object delete() {
        int delete = domeMapper.delete(new QueryWrapper<Dome>().lambda()
                .eq(Dome::getId, 1513013357774950400L)
                .between(Dome::getCreateDate, "2022-04-01 00:00:00", "2022-04-30 23:59:59"));
        return delete;
    }

}
