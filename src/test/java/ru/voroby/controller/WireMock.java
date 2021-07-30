package ru.voroby.controller;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

public class WireMock {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
}
