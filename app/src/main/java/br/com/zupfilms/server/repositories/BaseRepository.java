package br.com.zupfilms.server.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.zupfilms.model.ErrorMessage;
import okhttp3.ResponseBody;

abstract class BaseRepository {

    ErrorMessage serializeErrorBody(ResponseBody response){
        Gson gson = new Gson();
        Type type = new TypeToken<ErrorMessage>() {
        }.getType();
        return gson.fromJson(response.charStream(),type);
    }
}
