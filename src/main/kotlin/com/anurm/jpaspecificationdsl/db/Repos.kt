package com.anurm.jpaspecificationdsl.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface PersonRepo: JpaRepository<Person, Long>, JpaSpecificationExecutor<Person>

@Repository
interface ProductRepo: JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>

@Repository
interface OrderRepo: JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>

@Repository
interface OrderProductRepo: JpaRepository<OrderProduct, Long>, JpaSpecificationExecutor<OrderProduct>
