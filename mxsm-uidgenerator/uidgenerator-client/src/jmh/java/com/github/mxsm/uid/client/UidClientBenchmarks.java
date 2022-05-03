package com.github.mxsm.uid.client;

/**
 * @author mxsm
 * @date 2022/5/1 8:13
 * @Since 1.0.0
 */

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 5, time = 5)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
public class UidClientBenchmarks {

    private UidClient uidClient;

    @Setup
    public void init(){
            uidClient = UidClient.builder().setUidGeneratorServerUir("http://172.29.168.163:8080")
                .setSegmentNum(20).setThreshold(30).setMachineIdBits(4).setSequenceBits(18).build();
    }

    @Benchmark
    @Threads(4)
    public void uidClientGetUidBenchmarksThread4(Blackhole blackhole){
        blackhole.consume(uidClient.getSegmentUidFromCache("1"));
    }

    @Benchmark
    @Threads(8)
    public void uidClientGetUidBenchmarksThread8(Blackhole blackhole){
        blackhole.consume(uidClient.getSegmentUidFromCache("1"));
    }

    @Benchmark
    @Threads(50)
    public void uidClientGetUidBenchmarksThread50(Blackhole blackhole){
        blackhole.consume(uidClient.getSegmentUidFromCache("1"));
    }

    @TearDown
    public void shutdown(){
        uidClient.shutdown();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(UidClientBenchmarks.class.getSimpleName())
            .result("result.json")
            .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

}
