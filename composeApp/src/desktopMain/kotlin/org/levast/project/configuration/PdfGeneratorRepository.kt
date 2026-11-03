package org.levast.project.configuration

import Arme
import Armure
import Bouclier
import IListItem
import Monster
import Sort
import Special
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
        val bodyBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f)


        val titleCell = PdfPCell().apply {
            defaultCellStyle(this)
            horizontalAlignment = Element.ALIGN_CENTER
            verticalAlignment = Element.ALIGN_TOP
            setFixedHeight(fixedCardHeight)

            // Création d'une table interne pour mettre le titre et l'icône côte à côte
            val titleHeaderTable = PdfPTable(floatArrayOf(0.8f, 0.2f)).apply {
                widthPercentage = 100f
            }

            // Le Texte du Titre
            val textCell = PdfPCell(Phrase(item.nom.uppercase(), titleFont)).apply {
                border = Rectangle.NO_BORDER
                verticalAlignment = Element.ALIGN_MIDDLE
            }
            titleHeaderTable.addCell(textCell)

            // L'icône (Image)
            val iconCell = try {
                // Supposons que getImageDrawable() retourne le nom du fichier (ex: "drawable/mon_image.png")
                // Vous devez passer le chemin relatif au dossier resources
                val resourcePath = item.getImageUrl()
                val img = getPdfImageFromResource(resourcePath)

                if (img != null) {
                    img.scaleToFit(20f, 20f)
                    PdfPCell(img).apply {
                        border = Rectangle.NO_BORDER
                        horizontalAlignment = Element.ALIGN_RIGHT
                        verticalAlignment = Element.ALIGN_MIDDLE
                    }
                } else {
                    PdfPCell().apply { border = Rectangle.NO_BORDER }
                }
            } catch (e: Exception) {
                PdfPCell().apply { border = Rectangle.NO_BORDER }
            }
            titleHeaderTable.addCell(iconCell)

            // On ajoute cette mini-table à la cellule principale
            addElement(titleHeaderTable)
        }
        cardTable.addCell(titleCell)

        // 2. COLONNE STATISTIQUES
        val statsCell = PdfPCell().apply {
            defaultCellStyle(this)

            if(item is Arme) {
                stylingArmeCard(item, bodyFont, bodyBold)
            }else if(item is Sort){
                stylingSortCard(item,bodyFont, bodyBold)
            }else if(item is Special){
                stylingSpecialCard(item,bodyFont, bodyBold)
            }else if(item is Armure){
                stylingArmureCard(item,bodyFont, bodyBold)
            }else if(item is Bouclier){
                stylingBouclierCard(item,bodyFont, bodyBold)
            }else if(item is Monster){
                stylingMonsterCard(item,bodyFont, bodyBold)
            }
            else{
                // Utilise ta méthode getBody() qui contient la force, intelligence, etc.
                addElement(Paragraph(item.getBody(), bodyFont))
            }

        }
        cardTable.addCell(statsCell)

        // 3. COLONNE CAPACITÉ SPÉCIALE & HISTOIRE
        val infoCell = PdfPCell().apply {
            defaultCellStyle(this)

            // Section Histoire
            addElement(Paragraph(item.histoire, loreFont))

            if(item.capaciteSpeciale.isNotBlank()){
                addElement(Paragraph("\n")) // Espace
                addElement(Paragraph(item.capaciteSpeciale, abilityFont))
            }

        }
        cardTable.addCell(infoCell)

        return cardTable
    }

    private fun PdfPCell.stylingArmeCard(arme : Arme, bodyFont: Font, bodyBold: Font){
        addElement(Paragraph("Seuils :", bodyBold))
        addElement(Paragraph(arme.getTextSeuils(), bodyFont))
        addElement(Paragraph("\n")) // Espace

        if(arme.getTextCoupCritique().isNotBlank()){
            addElement(Paragraph("Coup critique : ", bodyBold))
            addElement(Paragraph(arme.getTextCoupCritiqueSansTitre(), bodyFont))
        }
        addInlineElement("Nombre de dés max : ", arme.maximumEnergie.toString(), bodyBold, bodyFont)
        addInlineElement("Poids : ", arme.poids.toString(), bodyBold, bodyFont)

    }

    private fun PdfPCell.stylingSortCard(sort : Sort, bodyFont: Font, bodyBold: Font){

        if(sort.getTextSeuils().isNotBlank()){
            addElement(Paragraph("Seuils :", bodyBold))
            addElement(Paragraph(sort.getTextSeuils(), bodyFont))
            addElement(Paragraph("\n")) // Espace
        }

        if(sort.getTextCoupCritique().isNotBlank()){
            addElement(Paragraph("Coup critique : ", bodyBold))
            addElement(Paragraph(sort.getTextCoupCritiqueSansTitre(), bodyFont))
        }
        addInlineElement("Utilisations : ", sort.utilisation.toString(), bodyBold, bodyFont)
        addInlineElement("Intelligence min : ", sort.intelligenceMin.toString(), bodyBold, bodyFont)

    }

    private fun PdfPCell.stylingSpecialCard(special : Special, bodyFont: Font, bodyBold: Font){
        addInlineElement("Type : ", special.itemType.name, bodyBold, bodyFont)
    }

    private fun PdfPCell.stylingBouclierCard(bouclier: Bouclier, bodyFont: Font, bodyBold: Font){
        addElement(Paragraph("Défenses :", bodyBold))
        addElement(Paragraph(bouclier.getDefenseText(), bodyFont))
        addElement(Paragraph("\n")) // Espace

        addInlineElement("Poids : ", bouclier.poids.toString(), bodyBold, bodyFont)

    }

    private fun PdfPCell.stylingArmureCard(armure: Armure, bodyFont: Font, bodyBold: Font){
        addElement(Paragraph("Défenses :", bodyBold))
        addElement(Paragraph(armure.getDefenseText(), bodyFont))
        addElement(Paragraph("\n")) // Espace

        addInlineElement("Poids : ", armure.poids.toString(), bodyBold, bodyFont)

    }

    private fun PdfPCell.stylingMonsterCard(monster: Monster, bodyFont: Font, bodyBold: Font){

        addInlineElement("Vie : ", monster.vie.toString(), bodyBold, bodyFont)
        addInlineElement("Force : ", monster.constructForceSeuils(), bodyBold, bodyFont)
        addElement(Paragraph("Défenses :", bodyBold))
        addElement(Paragraph(monster.getDefenseText(), bodyFont))
        addInlineElement("Intelligence : ", monster.intelligence.toString(), bodyBold, bodyFont)
        addInlineElement("Energie : ", monster.energie.toString(), bodyBold, bodyFont)
        addInlineElement("Âmes : ", monster.ames.toString(), bodyBold, bodyFont)

        addElement(Paragraph("\n")) // Espace

        addElement(Paragraph("Drops :", bodyBold))
        addElement(Paragraph(monster.constructListDropsString(), bodyFont))
    }

    private fun PdfPCell.addInlineElement(title:String, body:String, titleFont:Font, bodyFont: Font){
        val line = Paragraph().apply {
            add(Phrase(title, titleFont))
            add(Phrase(body, bodyFont))
        }
        addElement(line)
    }


    private fun getPdfImageFromResource(resourcePath: String): Image? {
        return try {
            val cleanPath = resourcePath.removePrefix("/")

            // Remplacez 'org.levast.project' par la valeur exacte de votre 'namespace' dans build.gradle.kts si nécessaire
            val packageName = "org.levast.project"
            val composeGeneratedPath = "composeResources/$packageName.generated.resources/$cleanPath"

            val pathsToTry = listOf(
                composeGeneratedPath,
                cleanPath,
                "composeResources/$cleanPath",
                "/$cleanPath"
            )

            var inputStream: java.io.InputStream? = null

            for (path in pathsToTry) {
                inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
                if (inputStream == null) {
                    inputStream = this::class.java.classLoader.getResourceAsStream(path)
                }
                if (inputStream != null) {
                    break
                }
            }

            // Si toujours rien, on tente un accès direct au système de fichiers (utile en mode Debug IDE)
            if (inputStream == null) {
                val devFile = java.io.File("src/commonMain/composeResources/$cleanPath")
                if (devFile.exists()) {
                    return Image.getInstance(devFile.absolutePath)
                }
            }

            val bytes = inputStream?.readAllBytes()
            if (bytes != null) {
                Image.getInstance(bytes)
            } else {
                println("PDF Error: Resource not found. Tried paths: $pathsToTry")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}