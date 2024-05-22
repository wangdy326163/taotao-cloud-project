package com.taotao.cloud.mq.common.support.hook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * rpc 关闭 hook
 * （1）可以添加对应的 hook 管理类
 *
 * @since 2024.05
 */
public abstract class AbstractShutdownHook implements RpcShutdownHook {

    /**
     * AbstractShutdownHook logger
     */
    private static final Log LOG = LogFactory.getLog(AbstractShutdownHook.class);

    @Override
    public void hook() {
        LOG.info("[Shutdown Hook] start");
        this.doHook();
        LOG.info("[Shutdown Hook] end");
    }

    /**
     * 执行 hook 操作
     * @since 2024.05
     */
    protected abstract void doHook();

}
