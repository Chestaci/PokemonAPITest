package com.github.Chestaci.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {
    private String name;
    private int weight;
    private List<PokemonAbility> abilities;
}
