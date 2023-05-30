package com.vecentek.back.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author 江淮项目特有错误原因
 */
public enum KeyErrorReasonEnumJac {

    /**
     * 车控故障 （OEM定义） 01 开头
     */
    ERROR_REASON_0101("0101", "主驾驶车门未关"),
    ERROR_REASON_0102("0102", "未进入安全模式"),
    ERROR_REASON_0103("0103", "点火钥匙错误"),
    ERROR_REASON_0104("0104", "车速不等于0，且大于4km/h"),
    ERROR_REASON_0105("0105", "安全认证请求超时"),
    ERROR_REASON_0106("0106", "发送KEY码后，等待认证结果超时"),
    ERROR_REASON_0107("0107", "读取鉴权参数错误"),
    ERROR_REASON_0108("0108", "TBOX判定与BCM认证失败"),
    ERROR_REASON_0109("0109", "控制鉴权失败次数超过2"),

    /**
     * IOS特有 蓝牙连接 02 开头
     */
    ERROR_REASON_02C2("02C2", "无效操作"),
    ERROR_REASON_02C3("02C3", "未连接"),
    ERROR_REASON_02C4("02C4", "离开范围"),
    ERROR_REASON_02C5("02C5", "操作取消"),
    ERROR_REASON_02C6("02C6", "连接超时"),
    ERROR_REASON_02C7("02C7", "设备断开"),
    ERROR_REASON_02CA("02CA", "连接失败"),
    ERROR_REASON_02CB("02CB", "连接达到限制数量"),
    ERROR_REASON_02CC("02CC", "操作不支持"),
    ERROR_REASON_02CE("02CE", "丢失配对信息"),
    ERROR_REASON_02CF("02CF", "加密超时"),
    ERROR_REASON_02D0("02D0", "LE配对设备过多"),

    /**
     * Android特有 蓝牙连接 02 开头
     */
    ERROR_REASON_0201("0201", "未知指令"),
    ERROR_REASON_0202("0202", "远程设备标识未知"),
    ERROR_REASON_0203("0203", "硬件故障"),
    ERROR_REASON_0204("0204", "配置参数超时"),
    ERROR_REASON_0205("0205", "配对失败"),
    ERROR_REASON_0206("0206", "没有PIN或KEY"),
    ERROR_REASON_0207("0207", "控制器内存不足，无法存储新参数"),
    ERROR_REASON_0208("0208", "连接超时"),
    ERROR_REASON_0209("0209", "连接数量达到上限"),
    ERROR_REASON_020A("020A", "同步连接数达到上限"),
    ERROR_REASON_020C("020C", "无法处理命令"),
    ERROR_REASON_020D("020D", "连接被拒绝，因为资源有限"),
    ERROR_REASON_020E("020E", "连接被拒绝，因为安全原因"),
    ERROR_REASON_020F("020F", "连接被拒绝，因为不可接受的MAC地址"),
    ERROR_REASON_0210("0210", "连接超时，因为远程设备没有响应或断开连接"),
    ERROR_REASON_0211("0211", "不支持的参数值"),
    ERROR_REASON_0212("0212", "无效的命令参数"),
    ERROR_REASON_0213("0213", "远程用户终止连接"),
    ERROR_REASON_0214("0214", "远程设备终止连接，因为资源不足"),
    ERROR_REASON_0215("0215", "远程设备终止连接，因为电源关闭"),
    ERROR_REASON_0216("0216", "连接被本地主机终止"),
    ERROR_REASON_0217("0217", "重复尝试"),
    ERROR_REASON_0218("0218", "连接被拒绝，因为配对不允许"),
    ERROR_REASON_0219("0219", "未知的LMP PDU"),
    ERROR_REASON_021A("021A", "未知的LMP PDU参数"),
    ERROR_REASON_021B("021B", "SCO或ESCO连接被拒绝，因为偏移量无效"),
    ERROR_REASON_021C("021C", "SCO或ESCO连接被拒绝，因为间隔无效"),
    ERROR_REASON_021D("021D", "SCO或ESCO连接被拒绝，因为空中模式无效"),
    ERROR_REASON_021E("021E", "无效的LMP或LL参数"),
    ERROR_REASON_021F("021F", "未指定错误"),
    ERROR_REASON_0220("0220", "不支持的LMP或LL参数"),
    ERROR_REASON_0221("0221", "控制器不允许更改角色"),
    ERROR_REASON_0222("0222", "LMP或LL响应超时"),
    ERROR_REASON_0223("0223", "LMP或LL错误，因为传输冲突"),
    ERROR_REASON_0224("0224", "LMP PDU不允许"),
    ERROR_REASON_0225("0225", "加密模式不接受"),
    ERROR_REASON_0226("0226", "不可交换连接KEY"),
    ERROR_REASON_0227("0227", "请求QOS不被支持"),
    ERROR_REASON_0228("0228", "意图已过去，无法执行 LMP PDU 或者 LL PDU"),
    ERROR_REASON_0229("0229", "无法配对，因为请求了单元密钥，因此不受支持"),
    ERROR_REASON_022A("022A", "启动了与正在进行的事务冲突的 LMP 事务或 LL 过程"),
    ERROR_REASON_022B("022B", "未知错误"),
    ERROR_REASON_022C("022C", "目前无法接受指定的服务质量参数，因此应终止服务质量协商"),
    ERROR_REASON_022D("022D", "服务质量被拒绝"),
    ERROR_REASON_022E("022E", "通道分类不支持"),
    ERROR_REASON_022F("022F", "连接被拒绝，因为安全原因"),
    ERROR_REASON_0230("0230", "参数超出范围"),
    ERROR_REASON_0231("0231", "未定义错误"),
    ERROR_REASON_0232("0232", "角色切换正在进行，但尚未完成"),
    ERROR_REASON_0234("0234", "连接被拒绝，因为已经达到了最大连接数"),
    ERROR_REASON_0235("0235", "角色切换失败"),
    ERROR_REASON_0236("0236", "由于响应数据包太大，控制器无法发送响应"),
    ERROR_REASON_0237("0237", "连接被拒绝，因为不支持安全简单配对"),
    ERROR_REASON_0238("0238", "连接被拒绝，因为主机正在忙于处理另一个配对操作"),
    ERROR_REASON_0239("0239", "连接被拒绝，因为没有可用的通道"),
    ERROR_REASON_023A("023A", "控制器忙"),
    ERROR_REASON_023B("023B", "连接参数不可接受"),
    ERROR_REASON_023C("023C", "广播超时"),
    ERROR_REASON_023D("023D", "连接终止，因为MIC失败"),
    ERROR_REASON_023E("023E", "LL启动了连接，但无法建立连接"),
    ERROR_REASON_023F("023F", "MAC连接失败"),
    ERROR_REASON_0240("0240", "粗略时钟调整被拒绝"),
    ERROR_REASON_0241("0241", "LMP PDU 被拒绝，因为当前未定义类型 0 子映射"),
    ERROR_REASON_0242("0242", "广播标识符未知"),
    ERROR_REASON_0243("0243", "连接被拒绝，因为已达到最大连接数"),
    ERROR_REASON_0244("0244", "操作被主机取消"),
    ERROR_REASON_0245("0245", "数据包太长"),
    ERROR_REASON_0285("0285", "由于未知的HCI命令，控制器无法处理命令包"),

    /**
     * 钥匙管理 03 开头
     */
    ERROR_REASON_0342("0342", "验证KR失败"),
    ERROR_REASON_0345("0345", "随机数校验失败"),
    ERROR_REASON_034B("034B", "钥匙已过期"),
    ERROR_REASON_0318("0318", "连接数已满"),
    ERROR_REASON_0301("0301", "认证超时"),
    ERROR_REASON_0302("0302", "认证失败"),
    ERROR_REASON_0303("0303", "版本协商失败,请升级APP版本"),
    ERROR_REASON_0304("0304", "离线可用次数为0,请同步钥匙"),
    ERROR_REASON_0305("0305", "激活超时"),
    ERROR_REASON_0306("0306", "激活失败"),
    ERROR_REASON_0307("0307", "RTC掉电同步失败,请到有网环境进行同步"),
    ERROR_REASON_0308("0308", "认证超时"),
    ERROR_REASON_0309("0309", "白名单认证失败,请到有网环境进行同步"),
    ERROR_REASON_030A("030A", "新的车主接入"),
    ERROR_REASON_030B("030B", "用户连接数已满,非车主钥匙无法进行连接"),
    ERROR_REASON_030C("030C", "白名单冻结,请到有网环境进行同步"),
    ERROR_REASON_030D("030D", "软件版本不兼容,请升级车端软件"),
    ERROR_REASON_030F("030F", "车端连接数已满,有车主接入");

    /**
     * type + reason
     */
    private final String code;

    /**
     * 操作名
     */
    private final String name;

    KeyErrorReasonEnumJac(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据type和reason 4位寻找匹配
     *
     * @param cmdCode 状态码
     * @return {@link String}
     */
    public static String matchReason(String cmdCode) {
        if (StrUtil.isBlank(cmdCode)) {
            return "未知原因";
        }
        for (KeyErrorReasonEnumJac reason : KeyErrorReasonEnumJac.values()) {
            if (cmdCode.equals(reason.getCode())) {
                return reason.getName();
            }
        }
        return "未知原因";
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}