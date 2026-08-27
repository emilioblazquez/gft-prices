Feature: Price API

  Scenario: Should return price list 1 at 10:00 on June 14
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-14T10:00:00Z'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response.productId == 35455
    And match response.brandId == 1
    And match response.priceList == 1
    And match response.price == 35.5
    And match response.currency == 'EUR'

  Scenario: Should return price list 2 at 16:00 on June 14
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-14T16:00:00Z'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response.productId == 35455
    And match response.brandId == 1
    And match response.priceList == 2
    And match response.price == 25.45
    And match response.currency == 'EUR'

  Scenario: Should return price list 1 at 21:00 on June 14
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-14T21:00:00Z'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response.productId == 35455
    And match response.brandId == 1
    And match response.priceList == 1
    And match response.price == 35.5
    And match response.currency == 'EUR'

  Scenario: Should return price list 3 at 10:00 on June 15
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-15T10:00:00Z'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response.productId == 35455
    And match response.brandId == 1
    And match response.priceList == 3
    And match response.price == 30.5
    And match response.currency == 'EUR'

  Scenario: Should return price list 4 at 21:00 on June 16
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-16T21:00:00Z'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 200
    And match response.productId == 35455
    And match response.brandId == 1
    And match response.priceList == 4
    And match response.price == 38.95
    And match response.currency == 'EUR'

  Scenario: Should return 404 when price does not exist
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-14T10:00:00Z'
    And param productId = 99999
    And param brandId = 1
    When method get
    Then status 404
    And match response.title == 'Price not found'
    And match response.status == 404
    And match response.detail == 'No applicable price found for brandId=1, productId=99999 and applicationDate=2020-06-14T10:00'
    And match response.instance == '/prices'

  Scenario: Should return 400 when applicationDate is missing
    Given url 'http://localhost:8080'
    And path 'prices'
    And param productId = 35455
    And param brandId = 1
    When method get
    Then status 400
    And match response.title == 'Missing request parameter'
    And match response.status == 400
    And match response.detail == "Required parameter 'applicationDate' is missing"
    And match response.instance == '/prices'

  Scenario: Should return 400 when productId has invalid type
    Given url 'http://localhost:8080'
    And path 'prices'
    And param applicationDate = '2020-06-14T10:00:00Z'
    And param productId = 'abc'
    And param brandId = 1
    When method get
    Then status 400
    And match response.title == 'Invalid request parameter'
    And match response.status == 400
    And match response.detail == "Parameter 'productId' has invalid value 'abc'"
    And match response.instance == '/prices'