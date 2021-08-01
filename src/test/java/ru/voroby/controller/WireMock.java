package ru.voroby.controller;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import ru.voroby.AbstractJUnit4Mockito;

public abstract class WireMock extends AbstractJUnit4Mockito {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
}
