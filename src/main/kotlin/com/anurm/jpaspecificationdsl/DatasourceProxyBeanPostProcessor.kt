package com.anurm.jpaspecificationdsl

import net.ttddyy.dsproxy.listener.logging.Log4jLogLevel
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel
import net.ttddyy.dsproxy.support.ProxyDataSource
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.framework.ProxyFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import javax.sql.DataSource

@Component
class DatasourceProxyBeanPostProcessor : BeanPostProcessor {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is DataSource && bean !is ProxyDataSource) {
            // Instead of directly returning a less specific datasource bean
            // (e.g.: HikariDataSource -> DataSource), return a proxy object.
            // See following links for why:
            //   https://stackoverflow.com/questions/44237787/how-to-use-user-defined-database-proxy-in-datajpatest
            //   https://gitter.im/spring-projects/spring-boot?at=5983602d2723db8d5e70a904
            //   https://arnoldgalovics.com/configuring-a-datasource-proxy-in-spring-boot/
            val factory = ProxyFactory(bean)
            factory.isProxyTargetClass = true
            factory.addAdvice(ProxyDataSourceInterceptor(bean))
            return factory.proxy
        }
        return bean
    }

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any = bean

    private class ProxyDataSourceInterceptor(dataSource: DataSource?) : MethodInterceptor {
        private val dataSource: DataSource

        init {
            this.dataSource = ProxyDataSourceBuilder.create(dataSource)
                .name("MyDS")
                .multiline()
                .writeIsolation()
                .logQueryBySlf4j(SLF4JLogLevel.INFO)
                .build()
        }

        @Throws(Throwable::class)
        override fun invoke(invocation: MethodInvocation): Any? {
            val proxyMethod = ReflectionUtils.findMethod(
                dataSource.javaClass,
                invocation.method.name
            )
            return if (proxyMethod != null) {
                proxyMethod.invoke(dataSource, *invocation.arguments)
            } else invocation.proceed()
        }
    }
}