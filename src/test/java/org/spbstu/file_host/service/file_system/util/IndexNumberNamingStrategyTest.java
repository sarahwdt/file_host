/*
package org.spbstu.file_host.service.file_system.util;

import org.spbstu.file_host.util.naming.IndexNumberNamingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class IndexNumberNamingStrategyTest {
    private final Stream<String> names = Stream.of("test1", "test1_0", "test1_1", "test1_2",
            "test2", "test2_0", "test2_1", "test2_3",
            "test3", "test3_1", "test3_2", "test3_3",
            "test4.txt", "test4_0.txt", "test4_1.txt", "test4_2.txt",
            "test5.txt", "test5_3.txt", "test5_0", "test5_2.txt");
    private final IndexNumberNamingStrategy indexNumberNamingStrategy = new IndexNumberNamingStrategy();

    @Test
    void applyNoneMatch() {
        Assertions.assertEquals(indexNumberNamingStrategy.apply(names, "test0"), "test0");
    }

    @Test
    void applyAppendLastCounter() {
        Assertions.assertEquals(indexNumberNamingStrategy.apply(names, "test1"), "test1_3");
    }

    @Test
    void applyAppendPreviousCounter() {
        Assertions.assertEquals(indexNumberNamingStrategy.apply(names, "test2"), "test2_2");
    }

    @Test
    void applyAppendFirstCounter() {
        Assertions.assertEquals(indexNumberNamingStrategy.apply(names, "test3"), "test3_0");
    }

    @Test
    void applyAppendLastCounterIgnoreExtension() {
        Assertions.assertEquals(indexNumberNamingStrategy.apply(names, "test4.txt"), "test4_3.txt");
    }

    @Test
    void applyAppendFirstCounterIgnoreExtension() {
        Assertions.assertEquals(indexNumberNamingStrategy.apply(names, "test5.txt"), "test5_0.txt");
    }
}*/
