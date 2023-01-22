package com.anurm.jpaspecificationdsl.specification

import jakarta.persistence.criteria.*
import kotlin.reflect.KProperty1

fun <Z, T, R> From<Z, T>.join(prop: KProperty1<T, R?>): Join<T, R> = join(prop.name)

fun <Z, T, R> From<Z, T>.joinSet(prop: KProperty1<T, Set<R>>): SetJoin<T, R> = joinSet(prop.name)

fun <Z, T, R> From<Z, T>.joinList(prop: KProperty1<T, List<R>>): ListJoin<T, R> = joinList(prop.name)

// Helper to enable get by Property
fun <T, R> Path<T>.get(prop: KProperty1<T, R?>): Path<R> = get(prop.name)