package com.github.ioloolo.live_ratio.service;

public class JinhakApply extends ApplyService {
	public JinhakApply(String name, String code, String totalSelector, String supportedSelector) {
		super(name, "http://addon.jinhakapply.com/RatioV1/RatioH/%s.html".formatted(code), totalSelector, supportedSelector);
	}
}
