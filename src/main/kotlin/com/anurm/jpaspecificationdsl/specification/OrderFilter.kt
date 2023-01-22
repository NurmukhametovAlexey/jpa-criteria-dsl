package com.anurm.jpaspecificationdsl.specification

import com.anurm.jpaspecificationdsl.db.Order
import com.anurm.jpaspecificationdsl.db.OrderProduct
import com.anurm.jpaspecificationdsl.db.Person
import com.anurm.jpaspecificationdsl.db.Product
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.util.*

class OrderFilter(

    val personId: Long? = null,
    val login: String? = null,
    val dateMin: Date? = null,
    val dateMax: Date? = null,
    val orderedCount: Int? = null,
    val productName: String? = null,
    val netPrice: BigDecimal? = null,

    ): Specification<Order> {

    override fun toPredicate(root: Root<Order>, query: CriteriaQuery<*>, cb: CriteriaBuilder): Predicate? {

        val predicates = LinkedList<Predicate>()

        dateMin?.let {
            predicates.add(
                cb.greaterThanOrEqualTo(root.get(Order::date), it)
            )
        }

        dateMax?.let {
            predicates.add(
                cb.lessThanOrEqualTo(root.get(Order::date), it)
            )
        }

        personId?.let {
            predicates.add(
                cb.equal(root.get(Order::person).get(Person::id), personId)
            )
        }

        val orderPersonJoin: Join<Order, Person> = root.join(Order::person)

        login?.let {
            predicates.add(
                cb.equal(orderPersonJoin.get(Person::login), it)
            )
        }

        val orderProductsJoin: SetJoin<Order, OrderProduct> = root.joinSet(Order::orderProducts)

//        orderedCount?.let {
//            predicates.add(
//                cb.equal(cb.count(orderProductsJoin), it)
//            )
//        }

        val productJoin = orderProductsJoin.join(OrderProduct::product)

        productName?.let {
            predicates.add(
                cb.equal(productJoin.get(Product::name), it)
            )
        }

        return cb.and(*predicates.toTypedArray())
    }
}