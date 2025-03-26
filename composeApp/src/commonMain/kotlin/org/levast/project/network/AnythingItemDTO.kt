package org.levast.project.network

import kotlinx.serialization.Serializable

@Serializable
class AnythingItemDTO(
    var typeItem: String? = null,
    var itemContent: String? = null
) {


}