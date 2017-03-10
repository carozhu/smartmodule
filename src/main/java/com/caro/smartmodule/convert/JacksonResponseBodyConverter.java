package com.caro.smartmodule.convert;//package com.caro.smartmodule.convert;
//import com.fasterxml.jackson.databind.ObjectReader;
//import java.io.IOException;
//import okhttp3.ResponseBody;
//import retrofit2.Converter;
//
//final class JacksonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
//    private final ObjectReader adapter;
//
//    JacksonResponseBodyConverter(ObjectReader adapter) {
//        this.adapter = adapter;
//    }
//
//    @Override public T convert(ResponseBody value) throws IOException {
//        try {
//            return adapter.readValue(value.charStream());
//        } finally {
//            value.close();
//        }
//    }
//}