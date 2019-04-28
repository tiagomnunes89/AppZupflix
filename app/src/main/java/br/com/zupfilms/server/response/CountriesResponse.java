package br.com.zupfilms.server.response;

import com.google.gson.annotations.SerializedName;

public class CountriesResponse {
    @SerializedName("iso_3166_1")
    String iso;
    @SerializedName("name")
    String name;

    public String getIso() {
        return iso;
    }

    public String getName() {
        return name;
    }
}
