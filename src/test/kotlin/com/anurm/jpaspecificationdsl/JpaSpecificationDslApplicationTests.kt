package com.anurm.jpaspecificationdsl

import com.anurm.jpaspecificationdsl.db.*
import com.anurm.jpaspecificationdsl.specification.OrderFilter
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@DataJpaTest(
    includeFilters = [ComponentScan.Filter(
        value = [DatasourceProxyBeanPostProcessor::class],
        type = FilterType.ASSIGNABLE_TYPE
    )],
    showSql = false
)
class JpaSpecificationDslApplicationTests {

    @Autowired
    private lateinit var orderRepo: OrderRepo
    @Autowired
    private lateinit var em: TestEntityManager

    @Test
    fun `should save everything`() {
        val person = em.persistFlushFind(Person(login = "Alice"))
        println(person)

        val o = Order(person = person)
        person.orders.add(o)
        val order = em.persistFlushFind(o)
        println(order)

        val book = em.persistFlushFind(Product(name = "Book", price = BigDecimal.TEN))
        println(book)

        val orderProduct = em.persistFlushFind(OrderProduct(order = order, product = book, quantity = 1))
        println(orderProduct)

        println(order)

        em.refresh(order)
        println("em.refresh(order)")

        println(order)

        val orderFilter = OrderFilter(
            personId = 1,
            login = "Alice",
            dateMin = Date.from(Instant.now().minusSeconds(100)),
            dateMax = Date.from(Instant.now().plusSeconds(100)),
            orderedCount = 2,
            productName = "Book",
            netPrice = BigDecimal.TEN,
        )

        val findAll = orderRepo.findAll(orderFilter)
        println("orderRepo.findAll(orderFilter)")
        println(findAll)
    }

}
