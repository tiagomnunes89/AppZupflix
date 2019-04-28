package br.com.zupfilms.server.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.zupfilms.model.ErrorMessage;
import okhttp3.ResponseBody;

public abstract class BaseRepository {

    protected int SUCCESS_CODE = 200;
    protected String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    protected String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    protected int SESSION_EXPIRED_CODE = 401;

    protected ErrorMessage serializeErrorBody(ResponseBody response){
        Gson gson = new Gson();
        Type type = new TypeToken<ErrorMessage>() {
        }.getType();
        return gson.fromJson(response.charStream(),type);
    }
}
