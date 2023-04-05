package com.github.Chestaci;

import com.github.Chestaci.model.Pokemon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Тесты проверки покемонов")
@Feature("Тесты проверки GET запросов")
public class APITest extends MyTest {

    /**
     * Параметризованный тест для осуществления проверки успешного получения покемона по имени
     *
     * @param name Имя покемона
     */
    @DisplayName("Проверка успешного получения покемона по имени")
    @Description("Параметризованный тест для осуществления проверки успешного получения покемона")
    @Story("Тест успешного получения покемона")
    @ParameterizedTest
    @ValueSource(strings = {"rattata", "pidgeotto"})
    public void getPokemonTest(String name) {
        getPokemonResponse(name).then().statusCode(200).contentType(ContentType.JSON);
    }


    /**
     * Параметризованный тест для осуществления проверки, что у покемона rattata,
     * в отличие от покемона pidgeotto, меньше вес и есть умение
     * (ability) побег (run-away)
     *
     * @param name1 Имя первого покемона
     * @param name2 Имя второго покемона
     */
    @DisplayName("Проверка сравнения двух покемонов по весу и способности убежать")
    @Description("Параметризованный тест для осуществления проверки сравнения " + "двух покемонов по весу и способности убежать")
    @Story("Тест сравнения двух покемонов")
    @ParameterizedTest
    @CsvSource(value = {"rattata, pidgeotto",}, ignoreLeadingAndTrailingWhitespace = true)
    public void weightAndRunAwayAbilityPokemonComparisonTest(String name1, String name2) {

        if (!pokemons.containsKey(name1)) {
            getPokemonJSON(name1, getPokemonResponse(name1));
        }
        if (!pokemons.containsKey(name2)) {
            getPokemonJSON(name2, getPokemonResponse(name2));
        }

        Pokemon pokemon1 = pokemons.get(name1);
        Pokemon pokemon2 = pokemons.get(name2);

        boolean weight = pokemon1.getWeight() < pokemon2.getWeight();

        boolean pokemonAbility1 = pokemon1.getAbilities().stream().anyMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));

        boolean pokemonAbility2 = pokemon2.getAbilities().stream().anyMatch(pokemonAbility -> pokemonAbility.getAbility().getName().equals("run-away"));

        boolean ability = (pokemonAbility1) && (!pokemonAbility2);

        Assertions.assertTrue(weight, "Вес покемона " + pokemon1.getName() + " больше, чем вес покемона " + pokemon2.getName());
        Assertions.assertTrue(ability, "У покемона " + pokemon1.getName() + " есть способность убежать: " + pokemonAbility1 + ", у покемона " + pokemon2.getName() + " есть способность убежать: " + pokemonAbility2);
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

        requestSpecification.pathParams("limit", limit).when().get("?limit={limit}/").then().statusCode(200).contentType(ContentType.JSON).body("results", Matchers.hasSize(limit)).body("results.name", Matchers.notNullValue());
    }
}
