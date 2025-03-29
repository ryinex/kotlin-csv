package com.ryinex.kotlin.csv

private class CsvColumn<T>(val title: String?, val mapper: (Int, T) -> Any)

class CsvBuilder<T> internal constructor(private val items: List<T>, private val isTitled: Boolean) {
    private val columns = arrayListOf<CsvColumn<T>>()

    fun column(title: String?, mapper: (index: Int, item: T) -> Any): CsvBuilder<T> {
        columns.add(CsvColumn(title, mapper))
        return this
    }

    fun build(name: String): CsvFile {
        val headers = if (isTitled) listOf(columns.map { it.title ?: " " }) else emptyList()
        val rows = headers + items.map { it.row() }
        val content = rows.content()
        return CsvFile(name, csvLines(content))
    }

    private fun T.row(): List<Any> = columns.mapIndexed { index, item -> item.mapper(index, this) }
}