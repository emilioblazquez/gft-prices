package com.gft.prices.e2e;

import com.intuit.karate.junit5.Karate;

class PricesE2ETest {

    @Karate.Test
    Karate testPrices() {
        return Karate.run("prices").relativeTo(getClass());
    }
}