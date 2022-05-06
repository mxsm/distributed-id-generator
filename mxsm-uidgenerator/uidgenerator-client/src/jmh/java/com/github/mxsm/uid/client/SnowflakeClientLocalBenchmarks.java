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
@Measurement(iterations = 3, time = 5)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SnowflakeClientLocalBenchmarks {

    private UidClient uidClient;

    @Setup
    public void init(){
            uidClient = UidClient.builder().setUidGeneratorServerUir("172.29.250.21:8080")
                .setSegmentNum(20).setThreshold(30).isSnowflakeUidFromRemote(false).build();
/*        uidClient = UidClient.builder().setUidGeneratorServerUir("http://192.168.3.21:8080")
                .setSegmentNum(20).setThreshold(30).isSnowflakeLocal(false).build();*/
    }

    @Benchmark
    @Threads(1)
    public void snowflakeClientLoaclBenchmarksThread1(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

    @Benchmark
    @Threads(4)
    public void snowflakeClientLoaclBenchmarksThread4(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }


    @Benchmark
    @Threads(8)
    public void snowflakeClientLoaclBenchmarksThread8(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

    @Benchmark
    @Threads(16)
    public void snowflakeClientLoaclBenchmarksThread16(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

    @Benchmark
    @Threads(32)
    public void snowflakeClientLoaclBenchmarksThread32(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

    @Benchmark
    @Threads(50)
    public void snowflakeClientLoaclBenchmarksThread50(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

    @Benchmark
    @Threads(100)
    public void snowflakeClientLoaclBenchmarksThread100(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

    @Benchmark
    @Threads(200)
    public void snowflakeClientLoaclBenchmarksThread200(Blackhole blackhole){
        blackhole.consume(uidClient.getSnowflakeUid());
    }

/*    @TearDown
    public void shutdown(){
        uidClient.shutdown();
    }*/

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(SnowflakeClientLocalBenchmarks.class.getSimpleName())
            .result("result.json")
            .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

}
