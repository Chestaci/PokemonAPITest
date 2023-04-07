package com.github.Chestaci;

import com.github.Chestaci.model.Pokemon;
import com.github.Chestaci.model.PokemonList;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;


@DisplayName("Тесты проверки покемонов")
@Feature("Тесты проверки GET запросов")
public class APITest extends MyTest {

    /**
     * Тест для осуществления проверки, что у покемона rattata,
     * в отличие от покемона pidgeotto, меньше вес и есть умение
     * (ability) побег (run-away)
     *
     * @param name1 Имя первого покемона
     * @param name2 Имя второго покемона
     */
    @DisplayName("Проверка сравнения двух покемонов по весу и способности убежать")
    @Description("Тест для осуществления проверки сравнения покемонов rattata и pidgeotto по весу и способности убежать")
    @Story("Тест сравнения двух покемонов")
    @ParameterizedTest
    @CsvSource(value = {"rattata, pidgeotto"}, ignoreLeadingAndTrailingWhitespace = true)
    public void weightAndRunAwayAbilityPokemonComparisonTest(String name1, String name2) {
        Pokemon pokemon1 = getPokemon(name1);
        Pokemon pokemon2 = getPokemon(name2);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(pokemon1.getWeight() < pokemon2.getWeight())
                .overridingErrorMessage("Полученный результат: вес покемона " + pokemon1.getName() +
                        " больше, чем вес покемона " + pokemon2.getName() + "." + "\n" + "Ожидаемый результат: вес покемона " + pokemon1.getName() +
                        " меньше, чем вес покемона " + pokemon2.getName() + ".")
                .isTrue();
        softAssertions.assertThat(pokemon1.getAbilities()
                .stream()
                .anyMatch(
                        pokemonAbility ->
                                pokemonAbility.getAbility().getName().equals("run-away")))
                .overridingErrorMessage("Полученный результат: у покемона " + pokemon1.getName() +
                        " нет способности убежать." + "\n" +"Ожидаемый результат: у покемона " +
                        pokemon1.getName() + " должна быть способность убежать.")
                .isTrue();
        softAssertions.assertThat(pokemon2.getAbilities()
                        .stream()
                        .noneMatch(
                                pokemonAbility ->
                                        pokemonAbility.getAbility().getName().equals("run-away")))
                .overridingErrorMessage("Полученный результат: у покемона " + pokemon2.getName() +
                        " есть способность убежать."  + "\n" + "Ожидаемый результат: у покемона " +
                        pokemon2.getName() + " не должно быть способности убежать.")
                .isTrue();
        softAssertions.assertAll();
  }

    /**
     * Параметризованный тест для осуществления проверки ограничения списка (limit) покемонов
     * и наличия имени у каждого покемона в ограниченном списке
     *
     * @param limit Ограничение списка покемонов
     */
    @DisplayName("Проверка ограничения списка покемонов и наличия имени")
    @Description("Параметризованный тест для осуществления проверки ограничения списка покемонов " + "и наличия имени у каждого покемона в ограниченном списке")
    @Story("Тест ограничения списка покемонов и проверки наличия имени")
    @ParameterizedTest
    @ValueSource(ints = {30, 50})
    public void listLimitCheckAndNameAvailabilityTest(int limit) {
        PokemonList list = given()
                .baseUri("https://pokeapi.co/api/v2/pokemon/")
                .pathParams("limit", limit)
                .when()
                .get("?limit={limit}/")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("results", Matchers.hasSize(limit))
                .extract()
                .body()
                .as(PokemonList.class);

        boolean hasName = list.getResults()
                .stream()
                .noneMatch(
                        pokemon ->
                                pokemon.getName().isEmpty() && pokemon.getName().isBlank()
                );

        Assertions.assertTrue(hasName, "Имя есть не у всех покемонов в ограниченном списке. " +
                "Ожидаемый результат: у всех покемонов в списке есть имя.");
    }
}
