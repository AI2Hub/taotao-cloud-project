package com.taotao.cloud.common.generator;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.taotao.cloud.common.support.generator.CSVFileGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;


public class CSVFileGeneratorTest {

    @Test
    public void testGenerate() throws IOException {
        String fileName = "tmp.csv";
        List<HashMap<String, Object>> data = Lists.newArrayList(
            new HashMap<>(ImmutableMap.<String, Object> of("1", "1a", "2", "2a",
                "3", "3a")),
            new HashMap<>(ImmutableMap.<String, Object> of("1", "1b", "2", "2b",
                "3", "3b")),
            new HashMap<>(ImmutableMap.<String, Object> of("1", "1c", "2", "2c",
                "3", "3c")));
        CSVFileGenerator.generate(data, new String[] { "1", "2", "3" },
            fileName);

        assertEquals(
            Lists.newArrayList("1a,2a,3a", "1b,2b,3b", "1c,2c,3c"),
            Files.readLines(new File(fileName), Charset.forName("utf-8")));
    }

}
