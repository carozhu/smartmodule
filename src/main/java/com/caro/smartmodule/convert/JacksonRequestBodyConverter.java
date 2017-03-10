package com.caro.smartmodule.convert;//package com.caro.smartmodule.convert;
//
//import com.fasterxml.jackson.databind.ObjectWriter;
//import java.io.IOException;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Converter;
//
//final class JacksonRequestBodyConverter<T> implements Converter<T, RequestBody> {
//    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
//
//    private final ObjectWriter adapter;
//
//    JacksonRequestBodyConverter(ObjectWriter adapter) {
//        this.adapter = adapter;
//    }
//
//    @Override public RequestBody convert(T value) throws IOException {
//        byte[] bytes = adapter.writeValueAsBytes(value);
//        return RequestBody.create(MEDIA_TYPE, bytes);
//    }
//}
