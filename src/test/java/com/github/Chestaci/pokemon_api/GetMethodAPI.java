package com.github.Chestaci.pokemon_api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;

import static io.restassured.RestAssured.given;

public class GetMethodAPI {

    private final String baseUri;

    public GetMethodAPI(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Метод для получения RequestSpecification
     *
     * @return RequestSpecification
     * @see RequestSpecification
     */
    @Step("Получение RequestSpecification")
    public RequestSpecification getRequestSpecification() {
        return given()
                .baseUri(baseUri);
    }

    /**
     * Метод для отправки запроса
     *
     * @param methodCall - метод запроса
     * @param nameParam  - параметры запроса
     * @param param      - значение параметров запроса
     * @return - ответ сервера (Response)
     */
    @Step("Отправка Get запроса и получение ответа от сервера")
    public Response getResponseCallMethod(String methodCall, String nameParam, String param) {
        return getRequestSpecification()
                .pathParams(nameParam, param)
                .when()
                .get(methodCall);
    }

    @Step("Проверка Content Type")
    public void checkContentType(Response response, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(response.contentType())
                .isEqualTo(ContentType.JSON.withCharset("utf-8"));
    }

    @Step("Проверка Status Code")
    public void checkStatusCode(Response response, SoftAssertions softAssertions) {
        softAssertions
                .assertThat(response.getStatusCode())
                .isEqualTo(200);
    }

}
