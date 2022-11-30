package wind.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wind.common.annotation.Log;
import wind.common.constant.CacheConstants;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.Result;
import wind.common.core.page.Paging;
import wind.common.enums.BusinessType;
import wind.common.utils.StreamUtils;
import wind.common.utils.StringUtils;
import wind.common.utils.redis.RedisUtils;
import wind.system.dto.OnlineDTO;
import wind.system.vo.OnlineVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/online")
public class OnlineController extends BaseController {

    /**
     * 获取在线用户监控列表
     *
     * @param ip       IP地址
     * @param userName 用户名
     */
    @SaCheckPermission("onlineList")
    @GetMapping("/list")
    public Result<Paging<OnlineVO>> list(String ip, String userName) {
        // 获取所有未过期的 token
        List<String> keys = StpUtil.searchTokenValue("", -1, 0);
        List<OnlineDTO> onlineDTOList = new ArrayList<>();
        for (String key : keys) {
            String token = key.replace(CacheConstants.LOGIN_TOKEN_KEY, "");
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActivityTimeoutByToken(token) < 0) {
                continue;
            }
            onlineDTOList.add(RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token));
        }
        if (StrUtil.isNotEmpty(ip) && StrUtil.isNotEmpty(userName)) {
            onlineDTOList = StreamUtils.filter(onlineDTOList, userOnline ->
                    StringUtils.equals(ip, userOnline.getIp()) &&
                            StringUtils.equals(userName, userOnline.getUsername())
            );
        } else if (StrUtil.isNotEmpty(ip)) {
            onlineDTOList = StreamUtils.filter(onlineDTOList, userOnline ->
                    StringUtils.equals(ip, userOnline.getIp())
            );
        } else if (StrUtil.isNotEmpty(userName)) {
            onlineDTOList = StreamUtils.filter(onlineDTOList, userOnline ->
                    StringUtils.equals(userName, userOnline.getUsername())
            );
        }
        Collections.reverse(onlineDTOList);
        onlineDTOList.removeAll(Collections.singleton(null));
        List<OnlineVO> userOnlineList = BeanUtil.copyToList(onlineDTOList, OnlineVO.class);
        return Result.ok(Paging.build(userOnlineList));
    }

    /**
     * 强退用户
     *
     * @param token token值
     */
    @SaCheckPermission("onlineList")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/quit")
    public Result<Void> forceLogout(String token) {
        try {
            StpUtil.kickoutByTokenValue(token);
        } catch (NotLoginException e) {
        }
        return Result.ok();
    }
}
