
package com.example.admin.chatterbox.model.giphyrand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiphyRandomResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
