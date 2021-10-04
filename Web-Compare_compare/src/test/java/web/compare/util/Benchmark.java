package web.compare.util;
import com.github.kilianB.benchmark.AlgorithmBenchmarker;
import com.github.kilianB.hashAlgorithms.AverageColorHash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import com.github.kilianB.matcher.categorize.supervised.LabeledImage;
import com.github.kilianB.matcher.exotic.SingleImageMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import web.compare.modell.MetadataHelper;

import java.io.File;
import java.nio.file.Path;
@Slf4j
public class Benchmark {

    public static void main(String[] args){
//Easy setup. Configure a SingleImageMatcher with the algorithms you want to test.

        SingleImageMatcher AverageMatcher = new SingleImageMatcher();
        SingleImageMatcher ColorMatcher = new SingleImageMatcher();
        SingleImageMatcher PerceptiveMatcher = new SingleImageMatcher();

//Naively add a bunch of algorithms using normalized hamming distance with a 40% threshold
        AverageMatcher.addHashingAlgorithm(new AverageHash(32), 0.1f);
        AverageMatcher.addHashingAlgorithm(new AverageHash(64), 0.1f);
        AverageMatcher.addHashingAlgorithm(new AverageHash(128), 0.1f);
        PerceptiveMatcher.addHashingAlgorithm(new PerceptiveHash(32), 0.1f);
        PerceptiveMatcher.addHashingAlgorithm(new PerceptiveHash(64), 0.1f);
        PerceptiveMatcher.addHashingAlgorithm(new PerceptiveHash(128), 0.1f);
        ColorMatcher.addHashingAlgorithm(new AverageColorHash(32), 0.1f);
        ColorMatcher.addHashingAlgorithm(new AverageColorHash(64), 0.1f);
        ColorMatcher.addHashingAlgorithm(new AverageColorHash(128), 0.1f);

//Create the benchmarker
        boolean includeSpeedMicrobenchmark = true;
        AlgorithmBenchmarker Avergagebm = new AlgorithmBenchmarker(AverageMatcher,false);
        AlgorithmBenchmarker Colorbm = new AlgorithmBenchmarker(ColorMatcher,false);
        AlgorithmBenchmarker Perceptivebm = new AlgorithmBenchmarker(PerceptiveMatcher,false);

//Add test images  Category label, image file
        var files = Path.of("C:\\Users\\Speick\\Desktop\\testIamges").toFile().list();
        if (files == null) {
            log.error("mainEnviroment was empty/not found");
        }
        assert files != null;
        for (var file : files) {
            log.info(file);
            if (FilenameUtils.getExtension(file).equals("png")) {
                Avergagebm.addTestImages(new LabeledImage(0, Path.of("C:\\Users\\Speick\\Desktop\\testIamges",file).toFile()));
                Colorbm.addTestImages(new LabeledImage(0, Path.of("C:\\Users\\Speick\\Desktop\\testIamges",file).toFile()));
                Perceptivebm.addTestImages(new LabeledImage(0, Path.of("C:\\Users\\Speick\\Desktop\\testIamges",file).toFile()));
            }
        }

//Enjoy your benchmark
//bm.display(); bm.toConsole();
        Avergagebm.toFile();
        Colorbm.toFile();
        Perceptivebm.toFile();
    }

}