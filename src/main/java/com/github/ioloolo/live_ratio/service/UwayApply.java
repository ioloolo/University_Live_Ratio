package com.github.ioloolo.live_ratio.service;

public class UwayApply extends ApplyService {
	public UwayApply(String name, String code, String totalSelector, String supportedSelector) {
		super(name, "http://ratio.uwayapply.com/%s".formatted(code), totalSelector, supportedSelector);
	}
}
