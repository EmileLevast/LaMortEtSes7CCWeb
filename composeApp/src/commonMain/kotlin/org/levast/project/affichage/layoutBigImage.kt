package org.levast.project.affichage

import IListItem
import Joueur
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import getNbrUtilisationAccordingItem
import lamortetses7ccweb.composeapp.generated.resources.Res
import lamortetses7ccweb.composeapp.generated.resources.UnknownImage
import org.jetbrains.compose.resources.painterResource
import org.levast.project.affichageMobile.layoutDetailJoueur
import org.levast.project.configuration.getGraphicConstants
import org.levast.project.configuration.getApiApp

@Composable
fun layoutBigImage(
    equipement: IListItem,
    onClick: (IListItem, Int) -> Unit,
    isShowingStats: Boolean,
    itemUtilisation: Int?,
    joueur: Joueur? = null,
) {
    val graphicsConsts = getGraphicConstants()
    var notesJoueur by remember { mutableStateOf("") }
    val apiApp = getApiApp()


    var nbrUtilisationItem by remember {
        mutableStateOf(
            getNbrUtilisationAccordingItem(
                equipement,
                itemUtilisation
            ).toInt()
        )
    }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {

            Column {

                LazyColumn(
                    modifier = Modifier.clickable {
                        onClick(equipement,nbrUtilisationItem)
                    }.padding(10.dp).weight(1f,false),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        Text(
                            text = equipement.nomComplet.ifBlank { equipement.nom },
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    item {
                        AsyncImage(
                            model = apiApp.createUrlImageFromItem(equipement),
                            contentDescription = null,
                            error = painterResource(Res.drawable.UnknownImage),
                        )
                    }

                    if (isShowingStats) {
                        item {
                            Text(
                                modifier = Modifier.padding(graphicsConsts.statsBigImagePadding),
                                text = equipement.getStatsAsStrings(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer

                            )
                        }
                    }else if (joueur!=null){
                        item {
                            notesJoueur = joueur.notesPnj[equipement.nom] ?: ""
                            layoutDetailJoueur(notesJoueur){ nouvelleNote ->
                                joueur.notesPnj[equipement.nom] = nouvelleNote //pour stocker ce qu'il faut mettre à jour
                                notesJoueur = nouvelleNote //pour faire le chgt d'affichage directement
                            }
                        }
                    }
                }

                if(isShowingStats){
                    val colorBackground =
                        MaterialTheme.colorScheme.tertiaryContainer //necessaire pour l utiliser dans la fonction de drawBehind
                    val colorFront =
                        MaterialTheme.colorScheme.tertiary //necessaire pour l utiliser dans la fonction de drawBehind

                    Row(
                        Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        OutlinedButton({
                            nbrUtilisationItem--
                        }) {
                            Text("-")
                        }

                        Text(
                            modifier = Modifier.drawBehind {
                                drawCircle(
                                    color = colorFront,
                                    radius = this.size.height / 2.2f
                                )
                                drawCircle(
                                    color = colorBackground,
                                    radius = this.size.height / 2.5f
                                )
                            },
                            text = nbrUtilisationItem.toString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.tertiary,
                        )

                        OutlinedButton({
                            nbrUtilisationItem++
                        }) {
                            Text("+")
                        }
                    }
                }

            }
        }
    }


}
