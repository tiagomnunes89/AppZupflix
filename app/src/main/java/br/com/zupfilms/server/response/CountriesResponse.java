package br.com.zupfilms.server.response;

import com.google.gson.annotations.SerializedName;

public class CountriesResponse {
    @SerializedName("iso_3166_1")
    private
    String iso;
    @SerializedName("name")
    private
    String name;

    public String getIso() {
        return iso;
    }

    public String getName() {
        return name;
    }
}
