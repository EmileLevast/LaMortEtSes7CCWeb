package org.levast.project.configuration

import IListItem
import org.levast.project.repository.IPdfGeneratorRepository
import kotlin.text.uppercase

import com.lowagie.text.* // Pour Document, PageSize, Rectangle, Element, Paragraph, Phrase
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.FileOutputStream

class PdfGeneratorRepository : IPdfGeneratorRepository  {
    override fun generatePdf(items: List<IListItem>) {
        // Définition des marges
        val margin = 10f

        // A4 = 210 x 297 mm. On réduit les marges pour maximiser l'espace des cartes
        val document = Document(PageSize.A4, 10f, 10f, 10f, 10f)
        PdfWriter.getInstance(document, FileOutputStream("items.pdf"))
        document.open()

        // Grille principale de 2 colonnes (pour faire 2x2 = 4 cartes par page)
        val mainGrid = PdfPTable(1).apply {
            widthPercentage = 100f
        }

        // Calcul de la hauteur exacte pour 4 cartes (Hauteur A4 - marges haut/bas)
        val fixedCardHeight = (document.pageSize.height - (margin * 2)-15f) / 4f

        items.forEach { arme ->
            val cardContainerCell = PdfPCell().apply {
                paddingTop = 5f
                paddingBottom = 5f
                paddingRight = 5f
                paddingLeft = 5f
                // On force la cellule du conteneur à la taille exacte
                setFixedHeight(fixedCardHeight)
                border = Rectangle.NO_BORDER

                // On ajoute la carte à l'intérieur de ce conteneur
                addElement(createItemCard(arme, fixedCardHeight))
            }
            mainGrid.addCell(cardContainerCell)
        }

        document.add(mainGrid)
        document.close()
    }

    private fun createItemCard(item: IListItem, fixedCardHeight: Float): PdfPTable {
        // La carte est un tableau de 3 colonnes
        // Colonne 1 (Titre) : 15% | Colonne 2 (Stats) : 35% | Colonne 3 (Capacités) : 50%
        val cardTable = PdfPTable(floatArrayOf(0.30f, 0.20f, 0.50f)).apply {
            widthPercentage = 100f

        }

        // --- STYLISATION DE LA CARTE (Bordure noire et Fond blanc) ---
        val defaultCellStyle = { cell: PdfPCell ->
            cell.backgroundColor = Color.WHITE
            cell.borderColor = Color.BLACK
            cell.borderWidth = 1f
            cell.paddingTop = 5f
            cell.paddingBottom = 5f
            cell.paddingRight = 5f
            cell.paddingLeft = 5f

        }

        val bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9f)
        val titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11f)
        val abilityFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f, Color(0xDFAF2C))
        val loreFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8f, Color.DARK_GRAY)


        // 1. COLONNE TITRE
        val titleCell = PdfPCell().apply {
            defaultCellStyle(this)
            horizontalAlignment = Element.ALIGN_CENTER
            verticalAlignment = Element.ALIGN_TOP
            setFixedHeight(fixedCardHeight)

            phrase = Phrase(item.nom.uppercase(), titleFont)
        }
        cardTable.addCell(titleCell)

        // 2. COLONNE STATISTIQUES
        val statsCell = PdfPCell().apply {
            defaultCellStyle(this)

            // Utilise ta méthode getBody() qui contient la force, intelligence, etc.
            addElement(Paragraph(item.getBody(), bodyFont))
        }
        cardTable.addCell(statsCell)

        // 3. COLONNE CAPACITÉ SPÉCIALE & HISTOIRE
        val infoCell = PdfPCell().apply {
            defaultCellStyle(this)

            val cap = if (item.histoire.isNotBlank()) item.histoire else "Aucune"
            addElement(Paragraph(cap, abilityFont))

            addElement(Paragraph("\n")) // Espace

            // Section Histoire
            addElement(Paragraph(item.histoire, loreFont))
        }
        cardTable.addCell(infoCell)

        return cardTable
    }

}