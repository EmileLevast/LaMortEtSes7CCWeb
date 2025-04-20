package org.levast.project.viewModel.stateviewmodel

import IListItem

data class AdminModelState(
    var listitems: List<IListItem> = listOf(),
    var listPinneditems: List<String> = listOf(),
    var filterAdminScreen: FilterAdminScreen = FilterAdminScreen.PLAYER
) {

}

enum class FilterAdminScreen{
    RESEARCH,
    EDIT,
    PLAYER
}