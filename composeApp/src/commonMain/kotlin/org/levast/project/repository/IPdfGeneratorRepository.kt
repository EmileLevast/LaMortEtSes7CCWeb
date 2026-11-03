package org.levast.project.repository

import IListItem

interface IPdfGeneratorRepository {
    fun generatePdf(items : List<IListItem>)
}