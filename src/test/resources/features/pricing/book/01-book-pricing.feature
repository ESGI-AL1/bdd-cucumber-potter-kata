Feature: Book Pricing

  Rule: No discount should be applied for a single book
    Scenario: Pricing for a single book
      Given my basket contains:
        | Harry Potter 1 |
      Then the basket price should be 8

  Rule: 5% discount should be applied for a 2 different Harry Potter books
    Scenario: Pricing for 2 different books
      Given my basket contains:
        | Harry Potter 1 |
        | Harry Potter 2 |
      Then 5 percent discount should be applied
      Then the basket price should be 15.20

  Rule: 10% discount should be applied for a 3 different Harry Potter books
    Scenario: Pricing for 3 different books
    Given my basket contains:
      | Harry Potter 1 |
      | Harry Potter 2 |
      | Harry Potter 3 |
    Then 10 percent discount should be applied
    Then the basket price should be 21.60

  Rule: 20% discount should be applied for a 4 different Harry Potter books
    Scenario: Pricing for 4 different books
    Given my basket contains:
      | Harry Potter 1 |
      | Harry Potter 2 |
      | Harry Potter 3 |
      | Harry Potter 4 |
    Then 20 percent discount should be applied
    Then the basket price should be 25.60

  Rule: 25% discount should be applied for a 5 different Harry Potter books
    Scenario: Pricing for 5 different books
    Given my basket contains:
      | Harry Potter 1 |
      | Harry Potter 2 |
      | Harry Potter 3 |
      | Harry Potter 4 |
      | Harry Potter 5 |
    Then 25 percent discount should be applied
    Then the basket price should be 30
    
  # Calcul à faire = 3 * 8
  # Explications = 3 livres uniques donc 3 * 8
  Rule: No discount should be applied for a set of similar books
    Scenario: Pricing for multiple copies of the same book
      Given my basket contains:
        | Harry Potter 1 |
        | Harry Potter 1 |
        | Harry Potter 1 |
    When there are 3 similar Harry Potter books
    Then the basket price should be 24.00

  # Calcul à faire = (3 * 8 * 0.90) + 8
  # Explications: 3 livres uniques (1, 2, 3) donc - 10% sur l'ensemble + 1 livre
  # Mauvais calcul: 2 livres pareils + 2 livres uniques -> (2 * 8) + (2 * 8 * 0.95)
  Scenario: Pricing of unique books and multiple copies of the same books
    Given my basket contains:
      | Harry Potter 1 |
      | Harry Potter 1 |
      | Harry Potter 2 |
      | Harry Potter 3 |
    When there are 2 similar Harry Potter books
    Then the basket price should be 29.60

  # Calcul à faire = ((4 * 8) * 0.80) + ((4 * 8) * 0.80)
  # Explications: 2 set de livres différents (1, 2, 3, 4) & (1, 2, 3, 5)
  Scenario: Pricing for the kata example
    Given my basket contains:
      | Harry Potter 1 |
      | Harry Potter 1 |
      | Harry Potter 2 |
      | Harry Potter 2 |
      | Harry Potter 3 |
      | Harry Potter 3 |
      | Harry Potter 4 |
      | Harry Potter 5 |
    When there are 3 similar Harry Potter books
    Then the basket price should be 51.20