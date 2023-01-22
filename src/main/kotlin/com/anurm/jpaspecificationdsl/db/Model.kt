package com.anurm.jpaspecificationdsl.db

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.Date

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val name: String,
    val price: BigDecimal
) {
    override fun toString(): String = "${javaClass.simpleName}: {id: $id, name: $name, price: $price}"
}

@Entity
@Table(name = "order_product")
class OrderProduct(

    @EmbeddedId
    var id: OrderProductIds = OrderProductIds(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("orderId")
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    val product: Product,

    val quantity: Int,
) {
    override fun toString(): String = "${javaClass.simpleName}: {id: $id, order: ${order.id}, product: ${product.name}, quantity: $quantity}"
}

@Embeddable
data class OrderProductIds (
    @Column(name = "order_id")
    var orderId: Long? = null,
    @Column(name = "product_id")
    var productId: Long? = null,
) : Serializable

@Entity
@Table(name = "orders")
@EntityListeners(value = [AuditingEntityListener::class])
class Order(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    val person: Person,

    @OneToMany(mappedBy = "order")
    val orderProducts: MutableSet<OrderProduct> = HashSet(),

    @CreatedDate
    var date: Date = Date.from(Instant.EPOCH)
) {
    override fun toString(): String = "${javaClass.simpleName}: {id: $id, date: $date, person: ${person.login}, orderProduct: ${orderProducts.map { it.id }}}"
}

@Entity
@Table(name = "person")
class Person(
    @Id
    @GeneratedValue
    val id: Long? = null,

    val login: String,

    @OneToMany(mappedBy = "person")
    val orders: MutableSet<Order> = HashSet()


    ) {
    override fun toString(): String = "${javaClass.simpleName}: {id: $id, login: $login}"
}