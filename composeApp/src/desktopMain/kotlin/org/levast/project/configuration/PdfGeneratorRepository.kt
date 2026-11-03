package org.levast.project.configuration

import Arme
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
        // A4 = 210 x 297 mm. On réduit les marges pour maximiser l'espace des cartes
        val document = Document(PageSize.A4, 10f, 10f, 10f, 10f)
        PdfWriter.getInstance(document, FileOutputStream("items.pdf"))
        document.open()

        // Grille principale de 2 colonnes (pour faire 2x2 = 4 cartes par page)
        val mainGrid = PdfPTable(2).apply {
            widthPercentage = 100f
        }

        items.forEach { arme ->
            val cardContainerCell = PdfPCell().apply {
                paddingTop = 10f
                paddingBottom = 10f
                paddingRight = 10f
                paddingLeft = 10f
                border = Rectangle.NO_BORDER
                // Calcul de la hauteur pour que 2 lignes tiennent pile sur une page A4
                // Hauteur A4 (842pt) - marges / 2
                minimumHeight = (PageSize.A4.height - 20f) / 2f

                // On ajoute la carte à l'intérieur de ce conteneur
                addElement(createWeaponCard(arme))
            }
            mainGrid.addCell(cardContainerCell)
        }

        // Si le nombre d'items est impair, on remplit la dernière case vide
        if (items.size % 2 != 0) {
            mainGrid.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
        }

        document.add(mainGrid)
        document.close()
    }

    private fun createWeaponCard(item: IListItem): PdfPTable {
        // La carte est un tableau de 3 colonnes
        // Colonne 1 (Titre) : 15% | Colonne 2 (Stats) : 35% | Colonne 3 (Capacités) : 50%
        val cardTable = PdfPTable(floatArrayOf(0.15f, 0.35f, 0.50f)).apply {
            widthPercentage = 100f
        }

        // --- STYLISATION DE LA CARTE (Bordure noire et Fond blanc) ---
        val defaultCellStyle = { cell: PdfPCell ->
            cell.backgroundColor = Color.WHITE
            cell.borderColor = Color.BLACK
            cell.borderWidth = 1.5f
            cell.paddingTop = 8f
            cell.paddingBottom = 8f
            cell.paddingRight = 8f
            cell.paddingLeft = 8f
        }

        // 1. COLONNE TITRE
        val titleCell = PdfPCell().apply {
            defaultCellStyle(this)
            horizontalAlignment = androidx.wear.compose.foundation.Element.ALIGN_CENTER
            verticalAlignment = androidx.wear.compose.foundation.Element.ALIGN_MIDDLE
            rotation = 90 // Rotation verticale pour un look "carte de jeu"

            val font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f,
                Color.BLACK)
            phrase = Phrase(item.nom.uppercase(), font)
        }
        cardTable.addCell(titleCell)

        // 2. COLONNE STATISTIQUES
        val statsCell = PdfPCell().apply {
            defaultCellStyle(this)
            val fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f)
            val fontText = FontFactory.getFont(FontFactory.HELVETICA, 8f)

            addElement(Paragraph("STATISTIQUES", fontTitle))
            addElement(Paragraph("________________", fontTitle))
            addElement(Paragraph("\n", fontText))
            // Utilise ta méthode getBody() qui contient la force, intelligence, etc.
            addElement(Paragraph(item.getBody(), fontText))
        }
        cardTable.addCell(statsCell)

        // 3. COLONNE CAPACITÉ SPÉCIALE & HISTOIRE
        val infoCell = PdfPCell().apply {
            defaultCellStyle(this)
            val fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f)
            val fontAbility = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8f,
                Color(0xFFDFAF2C.toInt())
            )
            val fontLore = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7f,
                Color.DARK_GRAY)

            // Section Capacité
            addElement(Paragraph("CAPACITÉ SPÉCIALE", fontTitle))
            val cap = if (item.histoire.isNotBlank()) item.histoire else "Aucune"
            addElement(Paragraph(cap, fontAbility))

            addElement(Paragraph("\n")) // Espace

            // Section Histoire
            addElement(Paragraph("HISTOIRE", fontTitle))
            addElement(Paragraph(item.histoire, fontLore))
        }
        cardTable.addCell(infoCell)

        return cardTable
    }

}