package com.mobile.countryapp.model

import com.fasterxml.jackson.annotation.JsonProperty

class CountryModel {
    @JsonProperty("title")
    var title: String? = null
    @JsonProperty("rows")
    var rows: List<Row>? = null
}

class Row {
    @JsonProperty("title")
    var title: String? = null
    @JsonProperty("description")
    var description: String? = null
    @JsonProperty("imageHref")
    var imageUrl: String? = null
}
