package com.github.ioloolo.live_ratio;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.github.ioloolo.live_ratio.service.ApplyService;
import com.github.ioloolo.live_ratio.service.JinhakApply;
import com.github.ioloolo.live_ratio.service.UwayApply;

import lombok.SneakyThrows;

public final class LiveRatio {
	private static final List<ApplyService> applies = List.of(
			new UwayApply("KOOKMIN", "Sl5KVyVNOWFhOUpmJSY6Jkp6ZlRm", "#Tr_0012_002620000 > td:nth-child(2)", "#Tr_0012_002620000 > td:nth-child(3)")
	);

	private static final List<ApplyService> previous = Collections.synchronizedList(new ArrayList<>());

	public static void main(String[] args) {
		Executors.newScheduledThreadPool(1)
				.scheduleAtFixedRate(() -> new LiveRatio().print(),
						0,
						1,
						TimeUnit.MINUTES
				);
	}

	@SneakyThrows({ExecutionException.class, InterruptedException.class})
	public void print() {
		ExecutorService executorService = Executors.newFixedThreadPool(applies.size());

		List<Future<?>> futures = new ArrayList<>();

		for (ApplyService apply : applies) {
			futures.add(executorService.submit(apply::fetchRatio));
		}

		for (Future<?> future : futures) {
			future.get();
		}

		for (int i = 0; i < 128; ++i) {
			System.out.println();
		}

		System.out.println("==========================================");
		System.out.println("     학교명       전체     지원      비율");
		for (ApplyService apply : applies) {
			ApplyService previous = LiveRatio.previous.stream()
					.filter(previousApply -> previousApply.getName().equals(apply.getName()))
					.findFirst()
					.orElse(null);

			System.out.printf("   %-10s %3d(%s%d) %3d(%s%d)   %.2f:1 %n",
					apply.getName(),
					apply.getTotal(),
					(previous == null ? 0 : previous.getTotal() - apply.getTotal()) >= 0 ? "+" : "-",
					previous == null ? 0 : previous.getTotal() - apply.getTotal(),
					apply.getSupported(),
					(previous == null ? 0 : previous.getSupported() - apply.getSupported()) >= 0 ? "+" : "-",
					previous == null ? 0 : previous.getSupported() - apply.getSupported(),
					apply.getSupported() == 0 ? 0 : apply.getSupported() / (double) apply.getTotal()
			);

			LiveRatio.previous.removeIf(previousApply -> previousApply.getName().equals(apply.getName()));
			LiveRatio.previous.add(apply);
		}
		System.out.println("==========================================");
		System.out.printf("(업데이트: %s)", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

		executorService.shutdown();
	}
}
