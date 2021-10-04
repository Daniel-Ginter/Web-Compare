package web.compare.util;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Hashtest
{
    public static void main(String[] args) throws Exception {
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(Path.of("C:\\Users\\Speick\\Desktop\\Result\\ls5vs013.cs.tu-dortmund.de\\home\\Chrome_test\\Chrome_test-a-5.png").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage cImage = null;
        try {
            cImage = ImageIO.read(Path.of("C:\\Users\\Speick\\Desktop\\1618863920840.jpg").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashingAlgorithm hasher = new PerceptiveHash(32);

        Hash bHash = hasher.hash(bImage);
        Hash cHash = hasher.hash(cImage);

        double similarityScore = bHash.normalizedHammingDistance(cHash);
        System.out.println(similarityScore);
        System.out.println(bHash.normalizedHammingDistance(bHash));


    }
}
