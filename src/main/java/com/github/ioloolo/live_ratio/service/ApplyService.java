package com.github.ioloolo.live_ratio.service;

import java.io.IOException;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Getter
@RequiredArgsConstructor
public abstract class ApplyService {
	private final String name;
	private final String url;
	private final String totalSelector;
	private final String supportedSelector;

	private int total = 0;
	private int supported = 0;

	@SneakyThrows(IOException.class)
	public void fetchRatio() {
		Document doc = Jsoup.connect(url).get();

		total = Integer.parseInt(Objects.requireNonNull(doc.selectFirst(totalSelector)).text());
		supported = Integer.parseInt(Objects.requireNonNull(doc.selectFirst(supportedSelector)).text());
	}
}
